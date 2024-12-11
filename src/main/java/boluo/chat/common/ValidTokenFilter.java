package boluo.chat.common;

import boluo.chat.config.ChatProperties;
import boluo.chat.domain.Manager;
import boluo.chat.mapper.ManagerMapper;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
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
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Setter
public class ValidTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedissonClient redissonClient;
    private AntPathMatcher matcher = new AntPathMatcher();
    @Resource
    private ChatProperties chatProperties;
    @Resource
    private ManagerMapper managerMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if(ignoreValid(req)) {
            //ignore valid
            chain.doFilter(req, res);
        }else {
            boolean isBasic = false;
            String token = req.getHeader("Authorization");
            if(StrUtil.isBlank(token)) {
                token = req.getParameter("Authorization");
            }else {
                token = token.trim();
                if(token.startsWith("Basic") && token.length() > 6) {
                    isBasic = true;
                    token = token.substring(6);
                }else if(token.startsWith("Bearer") && token.length() > 7){
                    token = token.substring(7);
                }else {//不支持的鉴权类型，赋值empty
                    token = StrUtil.EMPTY;
                }
            }
            try {
                if(StrUtil.isNotBlank(token) && validToken(token.trim(), isBasic)) {
                    chain.doFilter(req, res);
                }else {
                    res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    res.getWriter().write(JSONUtil.toJsonStr(ResVo.error(401, "Unauthorized")));
                }
            }finally {
                Session.clearSession();
            }
        }
    }

    private boolean ignoreValid(HttpServletRequest req) {
        HttpMethod method = HttpMethod.valueOf(req.getMethod());
        String url = req.getServletPath();
        return chatProperties.getIgnoreUrls().stream().anyMatch(it -> {
            if(it.contains(":")) {
                String[] arr = it.split(":");
                if(!method.matches(arr[0])) {
                    return false;
                }
                it = arr[1];
            }
            return matcher.match(it, url);
        });
    }

    private boolean validToken(String token, boolean isBasic) {
        try {
            if(isBasic) {
                String[] arr = Base64.decodeStr(token, Charset.defaultCharset()).split(":");
                if(arr.length != 2) {
                    return false;
                }
                String username = arr[0].trim();
                String password = arr[1].trim();
                Manager manager = managerMapper.selectByUsername(username);
                if(manager == null || manager.getState() == 0 || !BCrypt.checkpw(password, manager.getPassword())) {
                    return false;
                }
                ManagerSession session = new ManagerSession();
                session.setManagerId(manager.getId());
                session.setRole(SessionRoleEnum.Manager);
                session.setSignSecret(chatProperties.getManageSignSecret());
                Session.saveSession(session);
            }else {
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
            }
            return true;
        }catch (Throwable e) {
            log.error("valid token fail", e);
            return false;
        }
    }

}
