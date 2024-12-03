package boluo.chat.mapper;

import boluo.chat.domain.GroupApplyForm;
import boluo.chat.domain.GroupApplyFormStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface GroupApplyFormMapper extends BaseMapper<GroupApplyForm> {

    default boolean exists(Long tenantId, Long groupId, Long accountId) {
        LambdaQueryWrapper<GroupApplyForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupApplyForm::getTenantId, tenantId);
        queryWrapper.eq(GroupApplyForm::getGroupId, groupId);
        queryWrapper.eq(GroupApplyForm::getAccountId, accountId);
        queryWrapper.eq(GroupApplyForm::getDeleted, 0L);
        queryWrapper.ne(GroupApplyForm::getStatus, GroupApplyFormStatusEnum.Rejected);
        return exists(queryWrapper);
    }

}
