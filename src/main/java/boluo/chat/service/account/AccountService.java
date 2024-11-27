package boluo.chat.service.account;

import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.TenantMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private AccountMapper accountMapper;

}
