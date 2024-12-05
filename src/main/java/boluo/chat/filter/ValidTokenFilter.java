package boluo.chat.filter;

import boluo.chat.common.*;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Setter
public class ValidTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedissonClient redissonClient;
    private AntPathMatcher matcher = new AntPathMatcher();
    private List<String> ignoreUrls = List.of("/Tenants", "/Tenants/Login", "/Tenants/{tenantId}/Accounts/Login");

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if(ignoreValid(req)) {
            //ignore valid
            chain.doFilter(req, res);
        }else {
            String token = req.getHeader("Authorization");
            if(StrUtil.isNotBlank(token)) {
                token = req.getParameter("Authorization");
            }
            try {
                if(StrUtil.isNotBlank(token) && validToken(token)) {
                    chain.doFilter(req, res);
                }else {
                    res.getWriter().write(JSONUtil.toJsonStr(ResVo.error(401, "Unauthorized")));
                }
            }finally {
                Session.clearSession();
            }
        }
    }

    private boolean ignoreValid(HttpServletRequest req) {
        HttpMethod method = HttpMethod.valueOf(req.getMethod());
        if(!HttpMethod.POST.equals(method)) return false;
        String url = req.getServletPath();
        return ignoreUrls.stream().anyMatch(it -> {
            return matcher.match(it, url);
        });
    }

    private boolean validToken(String token) {
        try {
            token = token.substring(6).trim();
            JWTPayload jwtPayload = JWTUtil.parseToken(token).getPayload();
            JSONObject payload = jwtPayload.getClaimsJson();
            SessionRoleEnum role = SessionRoleEnum.valueOf(payload.getStr("role"));
            Session session;
            if(role == SessionRoleEnum.Manager) {
                session = null;
            }else if(role == SessionRoleEnum.Tenant) {
                Long tenantId = payload.getLong("tenantId");
                RBucket<TenantSession> bucket = redissonClient.getBucket(RedisKeyTool.tenantTokenRedisKey(tenantId));
                session = bucket.get();
            }else if(role == SessionRoleEnum.Account) {
                Long tenantId = payload.getLong("tenantId");
                String account = payload.getStr("account");
                RBucket<AccountSession> bucket = redissonClient.getBucket(RedisKeyTool.accountTokenRedisKey(tenantId, account));
                session = bucket.get();
            }else {
                return false;
            }
            if(session == null) return false;
            if(!JWTUtil.verify(token, session.getSignSecret().getBytes(StandardCharsets.UTF_8))) return false;
            Session.saveSession(session);
            return true;
        }catch (Throwable e) {
            log.error("valid token fail", e);
            return false;
        }
    }

}
