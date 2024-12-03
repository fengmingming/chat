package boluo.chat.service.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplyToJoinGroupCommand {

    @NotNull(message = "tenantId is null")
    private Long tenantId;
    @NotBlank(message = "groupId is null")
    private String groupId;
    @NotBlank(message = "account is null")
    private String account;

}
