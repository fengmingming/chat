package boluo.chat.service.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplyToAddFriendCommand {

    @NotBlank(message = "tenantId is blank")
    private Long tenantId;
    @NotBlank(message = "account is blank")
    private String account;
    @NotBlank(message = "applyAccount is blank")
    private String applyAccount;
    private String remark;

}
