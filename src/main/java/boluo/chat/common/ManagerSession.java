package boluo.chat.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerSession extends Session {

    private Long managerId;

    @Override
    public boolean verifyTenantId(Long tenantId) {
        return true;
    }

    @Override
    public boolean verifyAccount(String account) {
        return true;
    }

}
