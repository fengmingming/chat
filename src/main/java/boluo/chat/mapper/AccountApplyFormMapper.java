package boluo.chat.mapper;

import boluo.chat.domain.AccountApplyForm;
import boluo.chat.domain.AccountApplyFormStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface AccountApplyFormMapper extends BaseMapper<AccountApplyForm> {

    default boolean exists(Long tenantId, Long applyAccountId, Long accountId) {
        LambdaQueryWrapper<AccountApplyForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountApplyForm::getTenantId, tenantId)
                .eq(AccountApplyForm::getApplyAccountId, applyAccountId)
                .eq(AccountApplyForm::getAccountId, accountId)
                .ne(AccountApplyForm::getStatus, AccountApplyFormStatusEnum.Rejected.getCode())
                .eq(AccountApplyForm::getDeleted, 0L);
        return exists(queryWrapper);
    }

}
