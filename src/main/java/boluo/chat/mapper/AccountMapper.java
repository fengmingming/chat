package boluo.chat.mapper;

import boluo.chat.domain.Account;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.util.Assert;

import java.util.List;
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

    default List<Account> selectByAccount(Long tenantId, List<String> accounts) {
        Objects.requireNonNull(tenantId, "tenantId is null");
        Assert.isTrue(CollectionUtil.isNotEmpty(accounts), "accounts is empty");
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getTenantId, tenantId);
        queryWrapper.in(Account::getAccount, accounts);
        return selectList(queryWrapper);
    }

}
