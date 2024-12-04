package boluo.chat.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TenantSession extends Session {

    private List<Long> tenantIds;

    public TenantSession() {
        super(SessionRoleEnum.Tenant);
    }

}
