package boluo.chat.mapper;

import boluo.chat.domain.Relationship;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;

public interface RelationshipMapper extends MPJBaseMapper<Relationship> {

    default boolean isFriend(Long tenantId, Long accountId, Long friendAccountId) {
        return exists(new LambdaQueryWrapper<Relationship>().eq(Relationship::getTenantId, tenantId)
                .eq(Relationship::getAccountId, accountId).eq(Relationship::getFriendId, friendAccountId)
                .eq(Relationship::getDeleted, 0L))
                &&
                exists(new LambdaQueryWrapper<Relationship>().eq(Relationship::getTenantId, tenantId)
                .eq(Relationship::getAccountId, accountId).eq(Relationship::getFriendId, friendAccountId)
                .eq(Relationship::getDeleted, 0L));
    }

    default boolean canExchange(Long tenantId, Long accountId, Long friendAccountId) {
        LambdaQueryWrapper<Relationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Relationship::getTenantId, tenantId).eq(Relationship::getAccountId, accountId).eq(Relationship::getFriendId, friendAccountId).eq(Relationship::getDeleted, 0L);
        Relationship relationship = selectOne(queryWrapper);
        return relationship != null && relationship.getState() == 10;
    }

}
