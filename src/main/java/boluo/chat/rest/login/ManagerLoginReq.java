package boluo.chat.rest.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerLoginReq {

    @NotBlank(message = "username is blank")
    private String username;
    @NotBlank(message = "password is blank")
    private String password;
    @NotBlank(message = "phone is blank")
    private String phone;
    @NotBlank(message = "code is blank")
    private String code;

}
