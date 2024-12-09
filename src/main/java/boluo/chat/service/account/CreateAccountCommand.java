package boluo.chat.service.account;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateAccountCommand {

    @NotBlank(message = "account is blank")
    @Size(min = 1, max = 100)
    private String account;
    @Size(min = 8, max = 20)
    private String password;
    @Size(max = 100)
    private String nickName;
    @Size(max = 255)
    private String profilePicture;

}
