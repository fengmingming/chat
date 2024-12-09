package boluo.chat.service.account;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAccountCommand {

    @Size(min = 8, max = 20)
    private String password;
    @Size(max = 100)
    private String nickName;
    @Size(max = 255)
    private String profilePicture;

}
