package boluo.chat.service.account;

import boluo.chat.domain.FriendApplyFormStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateFriendApplyFormStatusCommand {

    @NotNull(message = "tenantId is null")
    private Long tenantId;
    /**
     * 操作人
     * */
    @NotBlank(message = "account is blank")
    private String account;
    /**
     * 申请id
     * */
    @NotNull(message = "friendApplyFormId is null")
    private Long friendApplyFormId;

    @NotNull(message = "status is null")
    private FriendApplyFormStatusEnum status;

}
