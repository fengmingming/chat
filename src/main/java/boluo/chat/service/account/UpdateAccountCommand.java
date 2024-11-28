package boluo.chat.service.account;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAccountCommand {

    @Min(8)
    @Max(20)
    private String password;
    @Max(100)
    private String nickName;
    @Max(255)
    private String profilePicture;

}
