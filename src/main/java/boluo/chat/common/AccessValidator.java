package boluo.chat.common;

import boluo.chat.domain.Account;
import boluo.chat.domain.Group;
import boluo.chat.domain.GroupMember;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.GroupMapper;
import boluo.chat.mapper.GroupMemberMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AccessValidator {

    @Resource
    private GroupMapper groupMapper;
    @Resource
    private GroupMemberMapper groupMemberMapper;
    @Resource
    private AccountMapper accountMapper;

    public boolean validGroup(Long tenantId, String groupId) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(groupId, "groupId is null");
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        return (group != null);
    }

    public boolean validGroupMember(Long tenantId, String groupId, String account) {
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

    public boolean validAccount(Long tenantId, String account) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(account, "account is null");
        Account accountEntity = accountMapper.selectByAccount(tenantId, account);
        return accountEntity != null;
    }

}
