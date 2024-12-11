package boluo.chat.common;

import boluo.chat.domain.Account;
import boluo.chat.mapper.AccountMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TenantSession extends Session {

    private List<Long> tenantIds;

    @Override
    public boolean verifyTenantId(Long tenantId) {
        return tenantIds.contains(tenantId);
    }

    @Override
    public boolean verifyAccount(String account) {
        AccountMapper accountMapper = SpringContext.getBean(AccountMapper.class);
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Account::getTenantId, tenantIds);
        queryWrapper.eq(Account::getAccount, account);
        return accountMapper.exists(queryWrapper);
    }

}
