package boluo.chat.service.tenant;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTenantCommand {

    @Size(min = 11, max = 11)
    private String phone;
    @Size(min = 6, max = 20)
    private String password;
    @Size(max = 100)
    private String tenantName;
    @Size(max = 256)
    private String signSecret;
    @Size(max = 2048)
    private String publicKey;
    @Size(max = 2048)
    private String privateKey;

}
