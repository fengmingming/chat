package boluo.chat.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTenantReq {

    @NotBlank(message = "phone is blank")
    @Max(11)
    private String phone;
    private String password;
    @NotBlank(message = "tenantName is blank")
    @Max(100)
    private String tenantName;

}
