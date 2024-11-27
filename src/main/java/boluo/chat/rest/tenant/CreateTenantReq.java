package boluo.chat.rest.tenant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTenantReq {

    @NotBlank(message = "phone is blank")
    @Max(11)
    private String phone;
    @Min(6)
    @Max(20)
    private String password;
    @NotBlank(message = "tenantName is blank")
    @Max(100)
    private String tenantName;

}
