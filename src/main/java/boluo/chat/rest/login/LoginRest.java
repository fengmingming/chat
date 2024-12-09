package boluo.chat.rest.login;

import boluo.chat.common.*;
import boluo.chat.domain.Account;
import boluo.chat.domain.Tenant;
import boluo.chat.exception.ImValidTokenException;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.TenantMapper;
import boluo.chat.service.login.LoginService;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class LoginRest {

    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private LoginService loginService;
    @Resource
    private RedissonClient redissonClient;

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

    @GetMapping("/validToken")
    public void validToken(@RequestParam("tenantId") Long tenantId, @RequestParam("account") String account, @RequestParam("token") String token, HttpServletResponse res) {
        try {
            //res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            JWTPayload jwtPayload = JWTUtil.parseToken(token).getPayload();
            JSONObject payload = jwtPayload.getClaimsJson();
            SessionRoleEnum role = SessionRoleEnum.valueOf(payload.getStr("role"));
            Session session;
            if(role == SessionRoleEnum.Manager) {
                session = null;
            }else if(role == SessionRoleEnum.Tenant) {
                if(!payload.getLong("tenantId").equals(tenantId)) {
                    res.setStatus(401);
                    return;
                }
                RBucket<TenantSession> bucket = redissonClient.getBucket(RedisKeyTool.tenantTokenRedisKey(tenantId));
                session = bucket.get();
            }else if(role == SessionRoleEnum.Account) {
                if(!payload.getLong("tenantId").equals(tenantId) || !payload.getStr("account").equals(account)) {
                    res.setStatus(401);
                    return;
                }
                RBucket<AccountSession> bucket = redissonClient.getBucket(RedisKeyTool.accountTokenRedisKey(tenantId, account));
                session = bucket.get();
            }else {
                res.setStatus(401);
                return;
            }
            if(session == null || !JWTUtil.verify(token, session.getSignSecret().getBytes(StandardCharsets.UTF_8))) {
                res.setStatus(401);
                return;
            }
            res.setStatus(200);
            res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            res.getWriter().write(JSONUtil.toJsonStr(MapUtil.builder()
                            .put("signSecret", session.getSignSecret())
                    .build()));
        }catch (Throwable t) {
            log.error("valid token fail", t);
            throw new ImValidTokenException(t);
        }
    }
}
