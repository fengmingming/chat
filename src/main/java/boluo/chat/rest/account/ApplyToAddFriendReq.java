package boluo.chat.rest.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplyToAddFriendReq {

    @NotBlank(message = "account is blank")
    private String account;
    private String remark;

}
