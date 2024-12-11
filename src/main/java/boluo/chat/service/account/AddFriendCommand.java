package boluo.chat.service.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddFriendCommand {

    @NotBlank(message = "friendAccount is blank")
    private String friendAccount;

}
