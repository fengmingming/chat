package boluo.chat.rest.tenant;

import boluo.chat.common.Paging;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindTenantsReq extends Paging {

    private String phone;
    private String tenantName;
    private Integer state;

}
