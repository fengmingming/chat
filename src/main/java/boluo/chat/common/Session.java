package boluo.chat.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Session {

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

}
