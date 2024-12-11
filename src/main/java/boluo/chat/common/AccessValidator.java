package boluo.chat.common;

import boluo.chat.domain.Account;
import boluo.chat.domain.Group;
import boluo.chat.domain.GroupMember;
import boluo.chat.domain.GroupMemberRoleEnum;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.GroupMapper;
import boluo.chat.mapper.GroupMemberMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 权限校验
 * */
@Component
@Setter
public class AccessValidator {

    @Resource
    private GroupMapper groupMapper;
    @Resource
    private GroupMemberMapper groupMemberMapper;
    @Resource
    private AccountMapper accountMapper;

    public boolean verifyGroup(Long tenantId, String groupId) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(groupId, "groupId is null");
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        return (group != null);
    }

    public void verifyGroupAndThrowException(Long tenantId, String groupId) {
        if(!verifyGroup(tenantId, groupId)) {
            throw new CodedException(403, "Forbidden");
        }
    }


    public boolean verifyGroupMember(Long tenantId, String groupId, String account) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(groupId, "groupId is null");
        Objects.requireNonNull(account, "account is null");
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        Account accountEntity = accountMapper.selectByAccount(tenantId, account);
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getTenantId, tenantId);
        queryWrapper.eq(GroupMember::getGroupId, group.getId());
        queryWrapper.eq(GroupMember::getAccountId, accountEntity.getId());
        queryWrapper.eq(GroupMember::getDeleted, 0L);
        return groupMemberMapper.exists(queryWrapper);
    }

    public void verifyGroupMemberAndThrowException(Long tenantId, String groupId, String account) {
        if(!verifyGroupMember(tenantId, groupId, account)) {
            throw new CodedException(403, "Forbidden");
        }
    }

    public boolean verifyAccount(Long tenantId, String account) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(account, "account is null");
        Account accountEntity = accountMapper.selectByAccount(tenantId, account);
        return Objects.nonNull(accountEntity);
    }

    public void verifyAccountAndThrowException(Long tenantId, String account) {
        if(!verifyAccount(tenantId, account)) {
            throw new CodedException(403, "Forbidden");
        }
    }

    public boolean verifyGroupMember(Long tenantId, String groupId, String account, GroupMemberRoleEnum roleEnum) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(groupId, "groupId is null");
        Objects.requireNonNull(account, "account is null");
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        Account accountEntity = accountMapper.selectByAccount(tenantId, account);
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getTenantId, tenantId);
        queryWrapper.eq(GroupMember::getGroupId, group.getId());
        queryWrapper.eq(GroupMember::getAccountId, accountEntity.getId());
        queryWrapper.eq(GroupMember::getRole, roleEnum.name());
        queryWrapper.eq(GroupMember::getDeleted, 0L);
        return groupMemberMapper.exists(queryWrapper);
    }

    public void verifyGroupMemberAndThrowException(Long tenantId, String groupId, String account, GroupMemberRoleEnum roleEnum) {
        if(!verifyGroupMember(tenantId, groupId, account, roleEnum)) {
            throw new CodedException(403, "Forbidden");
        }
    }

}
