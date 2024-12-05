package boluo.chat.rest.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountLoginReq {

    @NotBlank(message = "account is blank")
    private String account;
    @NotBlank(message = "password is blank")
    private String password;

}
