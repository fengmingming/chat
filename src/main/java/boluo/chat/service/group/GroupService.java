package boluo.chat.service.group;

import boluo.chat.common.TransactionalTool;
import boluo.chat.config.ChatProperties;
import boluo.chat.domain.*;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.GroupApplyFormMapper;
import boluo.chat.mapper.GroupMapper;
import boluo.chat.mapper.GroupMemberMapper;
import boluo.chat.message.ControlMessage;
import boluo.chat.rest.group.JoinGroupCommand;
import boluo.chat.rest.group.LeaveGroupCommand;
import boluo.chat.service.message.MessageService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.redisson.api.RedissonClient;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Setter
public class GroupService {

    @Resource
    private GroupMapper groupMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private GroupMemberMapper groupMemberMapper;
    @Resource
    private GroupApplyFormMapper groupApplyFormMapper;
    @Resource
    private MessageService messageService;
    @Resource
    private ChatProperties chatProperties;
    @Resource
    private RedissonClient redissonClient;

    @Transactional
    public Group createGroup(@NotNull Long tenantId, @Valid CreateGroupCommand command) {
        Group group = new Group();
        group.setTenantId(tenantId);
        group.setGroupId(command.getGroupId());
        group.setGroupName(command.getGroupName());
        group.setState(GroupStateEnum.Created.getCode());
        group.setDeleted(0L);
        group.setCreateTime(LocalDateTime.now());
        group.setUpdateTime(LocalDateTime.now());
        groupMapper.insert(group);
        addGroupMembers(tenantId, group.getId(), command.getAccounts());
        Account managerAccount = accountMapper.selectByAccount(tenantId, command.getManagerAccount());
        doUpdateGroupMemberRole(tenantId, group.getId(), managerAccount.getId(), GroupMemberRoleEnum.admin);
        return group;
    }

    @Transactional
    public void updateGroup(@NotNull Long tenantId, @NotNull String groupId, @Valid UpdateGroupCommand command) {
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        if(StrUtil.isNotBlank(command.getGroupName())) {
            group.setGroupName(command.getGroupName());
        }
        groupMapper.updateById(group);
    }

    @Transactional
    public void joinGroup(@NotNull Long tenantId, @NotNull String groupId, @NotNull @Valid JoinGroupCommand command) {
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        addGroupMembers(tenantId, group.getId(), command.getAccounts());
    }

    private void addGroupMembers(@NotNull Long tenantId,@NotNull Long groupId, @NotNull List<String> accounts) {
        if(!accounts.isEmpty()) {
            List<Long> accountIds = groupMemberMapper.selectAccountIds(tenantId, groupId);
            List<Account> accountEntities = accountMapper.selectByAccount(tenantId, accounts);
            List<GroupMember> gms = accountEntities.stream()
                    .filter(it -> !accountIds.contains(it.getId()))
                    .map(it -> {
                        GroupMember gm = new GroupMember();
                        gm.setTenantId(tenantId);
                        gm.setGroupId(groupId);
                        gm.setAccountId(it.getId());
                        gm.setRole(GroupMemberRoleEnum.member.name());
                        gm.setDeleted(0L);
                        gm.setCreateTime(LocalDateTime.now());
                        return gm;
                    }).toList();
            groupMemberMapper.insert(gms);
        }
    }

    private void doUpdateGroupMemberRole(Long tenantId, Long groupId, Long accountId, GroupMemberRoleEnum role) {
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getTenantId, tenantId).eq(GroupMember::getGroupId, groupId).eq(GroupMember::getAccountId, accountId).eq(GroupMember::getDeleted, 0L);
        GroupMember gm = groupMemberMapper.selectOne(queryWrapper);
        gm.setRole(role.name());
        groupMemberMapper.updateById(gm);
    }

    @Transactional
    public void disbandGroup(@NotNull Long tenantId,@NotNull String groupId) {
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        group.setState(GroupStateEnum.disband.getCode());
        group.setUpdateTime(LocalDateTime.now());
        groupMapper.updateById(group);
    }

    @Transactional
    public void leaveGroup(@NotNull Long tenantId,@NotNull String groupId, @NotNull @Valid LeaveGroupCommand command) {
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        List<Long> accountIds = accountMapper.selectByAccount(tenantId, command.getAccounts()).stream().map(Account::getId).toList();
        if(!accountIds.isEmpty()) {
            LambdaUpdateWrapper<GroupMember> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(GroupMember::getDeleted, System.currentTimeMillis());
            updateWrapper.eq(GroupMember::getTenantId, tenantId);
            updateWrapper.eq(GroupMember::getGroupId, group.getId());
            updateWrapper.in(GroupMember::getAccountId, accountIds);
            groupMemberMapper.update(updateWrapper);
        }
    }

    @Transactional
    public void applyToJoinGroup(@NotNull @Valid ApplyToJoinGroupCommand command) {
        Account account = accountMapper.selectByAccount(command.getTenantId(), command.getAccount());
        Group group = groupMapper.selectByGroupId(command.getTenantId(), command.getGroupId());
        if(!groupApplyFormMapper.exists(command.getTenantId(), group.getId(), account.getId())) {
            GroupApplyForm form = new GroupApplyForm();
            form.setTenantId(command.getTenantId());
            form.setGroupId(group.getId());
            form.setAccountId(account.getId());
            form.setStatus(GroupApplyFormStatusEnum.Applied.getCode());
            form.setCreateTime(LocalDateTime.now());
            form.setUpdateTime(LocalDateTime.now());
            groupApplyFormMapper.insert(form);
        }
    }

    @Transactional
    public void updateGroupApplyFormStatus(@Valid UpdateGroupApplyFormStatusCommand command) {
        Group group = groupMapper.selectByGroupId(command.getTenantId(), command.getGroupId());
        LambdaQueryWrapper<GroupApplyForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupApplyForm::getTenantId, command.getTenantId()).eq(GroupApplyForm::getGroupId, group.getId())
                .eq(GroupApplyForm::getId, command.getGroupApplyFormId()).eq(GroupApplyForm::getDeleted, 0L);
        GroupApplyForm form = groupApplyFormMapper.selectOne(queryWrapper);
        if(form != null && GroupApplyFormStatusEnum.findByCode(form.getStatus()) == GroupApplyFormStatusEnum.Applied) {
            form.setStatus(command.getStatus().getCode());
            form.setUpdateTime(LocalDateTime.now());
            groupApplyFormMapper.updateById(form);
            if(command.getStatus() == GroupApplyFormStatusEnum.Agreed) {
                if(StrUtil.isNotBlank(chatProperties.getGroupKeyTemplate())) {
                    SpelExpressionParser parser = new SpelExpressionParser();
                    TemplateParserContext context = new TemplateParserContext("{", "}");
                    String redisKey = parser.parseExpression(chatProperties.getGroupKeyTemplate(), context).getValue(group, String.class);
                    redissonClient.getBucket(redisKey).delete();
                }
                Account account = accountMapper.selectById(form.getAccountId());
                addGroupMembers(command.getTenantId(), group.getId(), List.of(account.getAccount()));
                ControlMessage message = new ControlMessage();
                message.setTenantId(command.getTenantId().toString());
                message.setFrom(command.getAccount());
                message.setTo("GROUP:" + group.getGroupId());
                message.setCommand(ControlMessage.REVIEW_GROUP_JOIN_REQUEST_NOTIFICATION);
                message.setArgs(new ArrayList<>(List.of(account.getAccount())));
                TransactionalTool.afterCommit(() -> {
                    messageService.sendMessage(message);
                });
            }
        }
    }

}
