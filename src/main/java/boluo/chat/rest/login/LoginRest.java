package boluo.chat.rest.login;

import boluo.chat.common.ResVo;
import boluo.chat.domain.Account;
import boluo.chat.domain.Tenant;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.TenantMapper;
import boluo.chat.service.login.LoginService;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.digest.BCrypt;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRest {

    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private LoginService loginService;

    @PostMapping("/Tenants/Login")
    public ResVo<?> tenantLogin(@RequestBody @Valid TenantLoginReq req) {
        Tenant tenant = tenantMapper.selectByPhone(req.getPhone());
        if(tenant == null || !BCrypt.checkpw(req.getPassword(), tenant.getPassword())) {
            return ResVo.error("用户名或密码错误");
        }
        String token = loginService.createToken(tenant);
        return ResVo.success(MapUtil.builder()
                .put("token", token).put("tenant", tenant)
                .build());
    }

    @PostMapping("/Tenants/{tenantId}/Accounts/Login")
    public ResVo<?> tenantLogin(@PathVariable("tenantId") Long tenantId, @RequestBody @Valid AccountLoginReq req) {
        Account account = accountMapper.selectByAccount(tenantId, req.getAccount());
        if(account == null || !BCrypt.checkpw(req.getPassword(), account.getPassword())) {
            return ResVo.error("用户名或密码错误");
        }
        String token = loginService.createToken(account);
        return ResVo.success(MapUtil.builder()
                .put("token", token).put("account", account)
                .build());
    }

}
