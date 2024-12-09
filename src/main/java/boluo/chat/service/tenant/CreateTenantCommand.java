package boluo.chat.service.tenant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTenantCommand {

    @NotBlank(message = "phone is blank")
    @Size(min = 11, max = 11)
    private String phone;
    @Size(min = 6, max = 20)
    private String password;
    @NotBlank(message = "tenantName is blank")
    @Size(max = 100)
    private String tenantName;

}
