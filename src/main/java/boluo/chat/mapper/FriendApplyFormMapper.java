package boluo.chat.mapper;

import boluo.chat.domain.FriendApplyForm;
import boluo.chat.domain.FriendApplyFormStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface FriendApplyFormMapper extends BaseMapper<FriendApplyForm> {

    default boolean exists(Long tenantId, Long applyAccountId, Long accountId) {
        LambdaQueryWrapper<FriendApplyForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendApplyForm::getTenantId, tenantId)
                .eq(FriendApplyForm::getApplyAccountId, applyAccountId)
                .eq(FriendApplyForm::getAccountId, accountId)
                .ne(FriendApplyForm::getStatus, FriendApplyFormStatusEnum.Rejected.getCode())
                .eq(FriendApplyForm::getDeleted, 0L);
        return exists(queryWrapper);
    }

}
