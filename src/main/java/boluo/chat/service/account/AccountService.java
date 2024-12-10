package boluo.chat.service.account;

import boluo.chat.common.TransactionalTool;
import boluo.chat.domain.*;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.FriendApplyFormMapper;
import boluo.chat.mapper.RelationshipMapper;
import boluo.chat.message.ControlMessage;
import boluo.chat.service.message.MessageService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Setter
@Validated
public class AccountService {

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private RelationshipMapper relationshipMapper;
    @Resource
    private FriendApplyFormMapper friendApplyFormMapper;
    @Resource
    private MessageService messageService;

    @Transactional
    public Account createAccount(@NotNull Long tenantId, @Valid CreateAccountCommand command) {
        Account account = new Account();
        account.setTenantId(tenantId);
        account.setAccount(command.getAccount());
        if(StrUtil.isNotBlank(command.getPassword())) {
            account.setPassword(BCrypt.hashpw(command.getPassword(), BCrypt.gensalt()));
        }
        account.setNickName(command.getNickName());
        account.setProfilePicture(command.getProfilePicture());
        account.setCreateTime(LocalDateTime.now());
        account.setUpdateTime(LocalDateTime.now());
        accountMapper.insert(account);
        return account;
    }

    @Transactional
    public void updateAccount(@NotNull Long tenantId, @NotBlank String account, @Valid UpdateAccountCommand command) {
        Account accountEntity = accountMapper.selectByAccount(tenantId, account);
        if(command.getPassword() != null) {
            accountEntity.setPassword(BCrypt.hashpw(command.getPassword(), BCrypt.gensalt()));
        }
        if(command.getNickName() != null) {
            accountEntity.setNickName(command.getNickName());
        }
        if(command.getProfilePicture() != null) {
            accountEntity.setProfilePicture(command.getProfilePicture());
        }
        accountEntity.setUpdateTime(LocalDateTime.now());
        accountMapper.updateById(accountEntity);
    }

    @Transactional
    public void addFriend(@NotNull Long tenantId, @NotBlank String account, @Valid AddFriendCommand command) {
        Account fromAccount = accountMapper.selectByAccount(tenantId, account);
        Account friendAccount = accountMapper.selectByAccount(tenantId, command.getFriendAccount());
        LambdaQueryWrapper<Relationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Relationship::getTenantId, tenantId);
        queryWrapper.eq(Relationship::getAccountId, fromAccount.getId());
        queryWrapper.eq(Relationship::getFriendId, friendAccount.getId());
        queryWrapper.eq(Relationship::getDeleted, 0L);
        if(!relationshipMapper.exists(queryWrapper)) {
            Relationship relationship = new Relationship();
            relationship.setTenantId(tenantId);
            relationship.setAccountId(fromAccount.getId());
            relationship.setFriendId(friendAccount.getId());
            relationship.setState(RelationshipStateEnum.NORMAL.getCode());
            relationship.setCreateTime(LocalDateTime.now());
            relationship.setUpdateTime(LocalDateTime.now());
            relationshipMapper.insert(relationship);
        }
    }

    @Transactional
    public void deleteFriend(@NotNull Long tenantId, @NotBlank String account, @Valid DeleteFriendCommand command) {
        Account fromAccount = accountMapper.selectByAccount(tenantId, account);
        Account friendAccount = accountMapper.selectByAccount(tenantId, command.getFriendAccount());
        LambdaQueryWrapper<Relationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Relationship::getTenantId, tenantId);
        queryWrapper.eq(Relationship::getAccountId, fromAccount.getId());
        queryWrapper.eq(Relationship::getFriendId, friendAccount.getId());
        queryWrapper.eq(Relationship::getDeleted, 0L);
        Relationship relationship = relationshipMapper.selectOne(queryWrapper);
        if(relationship != null) {
            relationship.setDeleted(System.currentTimeMillis());
            relationshipMapper.updateById(relationship);
        }
    }

    @Transactional
    public void applyToAddFriend(@Valid ApplyToAddFriendCommand command) {
        Account applyAccount = accountMapper.selectByAccount(command.getTenantId(), command.getApplyAccount());
        Account account = accountMapper.selectByAccount(command.getTenantId(), command.getAccount());
        if(!friendApplyFormMapper.exists(command.getTenantId(), applyAccount.getId(), account.getId())) {
            FriendApplyForm form = new FriendApplyForm();
            form.setTenantId(command.getTenantId());
            form.setApplyAccountId(applyAccount.getId());
            form.setAccountId(account.getId());
            form.setStatus(FriendApplyFormStatusEnum.Applied.getCode());
            form.setCreateTime(LocalDateTime.now());
            form.setUpdateTime(LocalDateTime.now());
            friendApplyFormMapper.insert(form);
        }
    }

    @Transactional
    public void updateFriendApplyFormStatus(UpdateFriendApplyFormStatusCommand command) {
        Account accountEntity = accountMapper.selectByAccount(command.getTenantId(), command.getAccount());
        LambdaQueryWrapper<FriendApplyForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendApplyForm::getId, command.getFriendApplyFormId())
                .eq(FriendApplyForm::getTenantId, command.getTenantId())
                .eq(FriendApplyForm::getAccountId, accountEntity.getId())
                .eq(FriendApplyForm::getStatus, FriendApplyFormStatusEnum.Applied.getCode())
                .eq(FriendApplyForm::getDeleted, 0L);
        FriendApplyForm form = friendApplyFormMapper.selectOne(queryWrapper);
        if(form != null) {
            form.setStatus(command.getStatus().getCode());
            form.setUpdateTime(LocalDateTime.now());
            friendApplyFormMapper.updateById(form);
            if(command.getStatus() == FriendApplyFormStatusEnum.Agreed) {
                Account toAccount = accountMapper.selectById(form.getApplyAccountId());
                ControlMessage message = new ControlMessage();
                message.setTenantId(command.getTenantId().toString());
                message.setFrom(command.getAccount());
                message.setTo(toAccount.getAccount());
                message.setCommand(ControlMessage.REVIEW_FRIEND_REQUEST_NOTIFICATION);
                message.setArgs(new ArrayList<>());
                TransactionalTool.afterCommit(() -> {
                    messageService.recordAndSendMessage(message);
                });
            }
        }
    }

}
