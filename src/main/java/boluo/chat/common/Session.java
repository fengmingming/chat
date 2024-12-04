package boluo.chat.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Session {

    private final SessionRoleEnum sessionRole;

    @JsonCreator
    public Session(@JsonProperty("sessionRole") SessionRoleEnum sessionRole) {
        this.sessionRole = sessionRole;
    }

    public boolean isManager() {
        return sessionRole == SessionRoleEnum.Manager;
    }

    public boolean isTenant() {
        return sessionRole == SessionRoleEnum.Tenant;
    }

    public boolean isAccount() {
        return sessionRole == SessionRoleEnum.Account;
    }

}
