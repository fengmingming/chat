package boluo.chat.service.group;

import boluo.chat.domain.Account;
import boluo.chat.domain.Group;
import boluo.chat.domain.GroupMember;
import boluo.chat.domain.GroupStateEnum;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.GroupMapper;
import boluo.chat.mapper.GroupMemberMapper;
import boluo.chat.rest.group.JoinGroupCommand;
import boluo.chat.rest.group.LeaveGroupCommand;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
public class GroupService {

    @Resource
    private GroupMapper groupMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private GroupMemberMapper groupMemberMapper;

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
                        gm.setDeleted(0L);
                        gm.setCreateTime(LocalDateTime.now());
                        return gm;
                    }).toList();
            groupMemberMapper.insert(gms);
            //存在的修改状态
            List<Long> existAccountIds = accountEntities.stream().map(Account::getId).filter(accountIds::contains).toList();
            if(!existAccountIds.isEmpty()) {
                LambdaUpdateWrapper<GroupMember> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(GroupMember::getDeleted, 0L);
                updateWrapper.eq(GroupMember::getTenantId, tenantId);
                updateWrapper.eq(GroupMember::getGroupId, groupId);
                updateWrapper.in(GroupMember::getAccountId, existAccountIds);
                groupMemberMapper.update(updateWrapper);
            }
        }
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
            updateWrapper.eq(GroupMember::getGroupId, groupId);
            updateWrapper.in(GroupMember::getAccountId, accountIds);
            groupMemberMapper.update(updateWrapper);
        }
    }

}
