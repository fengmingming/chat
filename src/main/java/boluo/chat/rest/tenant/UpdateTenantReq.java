package boluo.chat.rest.tenant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTenantReq {

    @Max(11)
    @Min(11)
    private String phone;
    @Min(6)
    @Max(20)
    private String password;
    @Max(100)
    private String tenantName;
    @Max(256)
    private String signSecret;
    @Max(2048)
    private String publicKey;
    @Max(2048)
    private String privateKey;

}
