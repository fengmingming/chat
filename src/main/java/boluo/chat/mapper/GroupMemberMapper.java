package boluo.chat.mapper;

import boluo.chat.domain.GroupMember;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Objects;

public interface GroupMemberMapper extends BaseMapper<GroupMember> {

    default List<Long> selectAccountIds(Long tenantId, Long groupId) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(groupId, "groupId is null");
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getTenantId, tenantId);
        queryWrapper.eq(GroupMember::getGroupId, groupId);
        return selectList(queryWrapper).stream().map(GroupMember::getAccountId).toList();
    }

    default GroupMember selectByAccountId(Long tenantId, Long groupId, Long accountId) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(groupId, "groupId is null");
        Objects.requireNonNull(accountId, "accountId is null");
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getTenantId, tenantId).eq(GroupMember::getGroupId, groupId).eq(GroupMember::getAccountId, accountId).eq(GroupMember::getDeleted, 0L);
        return selectOne(queryWrapper);
    }

}
