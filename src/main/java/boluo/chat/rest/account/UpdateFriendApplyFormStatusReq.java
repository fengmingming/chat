package boluo.chat.rest.account;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateFriendApplyFormStatusReq {

    @NotNull
    private Integer status;

}
