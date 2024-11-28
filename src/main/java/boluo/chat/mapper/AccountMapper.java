package boluo.chat.mapper;

import boluo.chat.domain.Account;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Objects;

public interface AccountMapper extends BaseMapper<Account> {

    default Account selectByAccount(Long tenantId, String account) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Objects.requireNonNull(account, "account is null");
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getTenantId, tenantId);
        queryWrapper.eq(Account::getAccount, account);
        return selectOne(queryWrapper);
    }

}
