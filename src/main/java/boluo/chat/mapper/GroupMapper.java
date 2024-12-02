package boluo.chat.mapper;

import boluo.chat.domain.Group;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Objects;

public interface GroupMapper extends BaseMapper<Group> {

    default Group selectByGroupId(Long tenantId, String groupId) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(groupId, "groupId is null");
        LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Group::getTenantId, tenantId);
        queryWrapper.eq(Group::getGroupId, groupId);
        return selectOne(queryWrapper);
    }

}
