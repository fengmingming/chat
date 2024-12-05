package boluo.chat.rest.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TenantLoginReq {

    @NotBlank(message = "phone is blank")
    private String phone;
    @NotBlank(message = "password is blank")
    private String password;

}
