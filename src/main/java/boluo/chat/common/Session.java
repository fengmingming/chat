package boluo.chat.common;

import boluo.chat.domain.Account;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Session {

    private static ThreadLocal<Session> cache = new ThreadLocal<>();

    public static Session currentSession() {
        return cache.get();
    }

    static void saveSession(Session session) {
        cache.set(session);
    }

    static void clearSession() {
        cache.remove();
    }

    private String signSecret;
    private SessionRoleEnum role;

    public boolean isManager() {
        return role == SessionRoleEnum.Manager;
    }

    public boolean isTenant() {
        return role == SessionRoleEnum.Tenant;
    }

    public boolean isAccount() {
        return role == SessionRoleEnum.Account;
    }

    public abstract boolean verifyTenantId(Long tenantId);

    public abstract boolean verifyAccount(String account);

    public Session verifyTenantIdAndThrowException(Long tenantId) {
        if(!verifyTenantId(tenantId)) {
            throw new CodedException(403, "Forbidden");
        }
        return this;
    }

    public Session verifyAccountAndThrowException(String account) {
        if(!verifyAccount(account)) {
            throw new CodedException(403, "Forbidden");
        }
        return this;
    }

}
