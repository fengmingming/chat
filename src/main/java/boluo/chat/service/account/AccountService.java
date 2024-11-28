package boluo.chat.service.account;

import boluo.chat.domain.Account;
import boluo.chat.domain.Relationship;
import boluo.chat.domain.RelationshipStateEnum;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.RelationshipMapper;
import boluo.chat.mapper.TenantMapper;
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

@Service
@Setter
@Validated
public class AccountService {

    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private RelationshipMapper relationshipMapper;

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

}
