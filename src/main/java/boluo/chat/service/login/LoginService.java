package boluo.chat.service.login;

import boluo.chat.common.*;
import boluo.chat.config.ChatProperties;
import boluo.chat.domain.Account;
import boluo.chat.domain.Manager;
import boluo.chat.domain.Tenant;
import boluo.chat.mapper.TenantMapper;
import cn.hutool.core.map.MapUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Setter
@Service
public class LoginService {

    @Resource
    private ChatProperties chatProperties;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private TenantMapper tenantMapper;

    public String createToken(Manager manager) {
        ManagerSession session = new ManagerSession();
        session.setManagerId(manager.getId());
        session.setRole(SessionRoleEnum.Manager);
        session.setSignSecret(chatProperties.getManageSignSecret());
        String token = JWTUtil.createToken(MapUtil.builder("managerId", (Object) manager.getId())
                .put("role", session.getRole().name())
                .put("expire", System.currentTimeMillis() + (chatProperties.getTimeout() * 60 * 60 * 1000))
                .build(), chatProperties.getManageSignSecret().getBytes(StandardCharsets.UTF_8));
        RBucket<ManagerSession> bucket = redissonClient.getBucket(RedisKeyTool.tenantTokenRedisKey(manager.getId()));
        bucket.set(session, Duration.ofHours(chatProperties.getTimeout()));
        return token;
    }

    public String createToken(Tenant tenant) {
        TenantSession session = new TenantSession();
        session.setRole(SessionRoleEnum.Tenant);
        session.setSignSecret(tenant.getSignSecret());
        session.setTenantIds(new ArrayList<>(List.of(tenant.getTenantId())));
        String token = JWTUtil.createToken(MapUtil.builder("tenantId", (Object) tenant.getTenantId())
                .put("role", session.getRole().name())
                .put("expire", System.currentTimeMillis() + (tenant.getTimeout() * 60 * 60 * 1000))
                .build(), tenant.getSignSecret().getBytes(StandardCharsets.UTF_8));
        RBucket<TenantSession> bucket = redissonClient.getBucket(RedisKeyTool.tenantTokenRedisKey(tenant.getTenantId()));
        bucket.set(session, Duration.ofHours(tenant.getTimeout()));
        return token;
    }

    public String createToken(Account account) {
        Tenant tenant = tenantMapper.selectById(account.getTenantId());
        SimpleAccount simpleAccount = new SimpleAccount();
        simpleAccount.setTenantId(account.getTenantId());
        simpleAccount.setAccount(account.getAccount());
        AccountSession session = new AccountSession();
        session.setRole(SessionRoleEnum.Account);
        session.setSignSecret(tenant.getSignSecret());
        session.setAccounts(new ArrayList<>(List.of(simpleAccount)));
        String token = JWTUtil.createToken(MapUtil.builder("tenantId", (Object) account.getTenantId())
                .put("role", session.getRole().name())
                .put("account", account.getAccount()).put("expire", System.currentTimeMillis() + (tenant.getTimeout() * 60 * 60 * 1000))
                .build(), tenant.getSignSecret().getBytes(StandardCharsets.UTF_8));
        RBucket<AccountSession> bucket = redissonClient.getBucket(RedisKeyTool.accountTokenRedisKey(account.getTenantId(), account.getAccount()));
        bucket.set(session, Duration.ofHours(tenant.getTimeout()));
        return token;
    }

}
