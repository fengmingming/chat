package boluo.chat.rest.account;

import boluo.chat.common.validation.IntEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateFriendApplyFormStatusReq {

    @NotNull
    @IntEnum(value = {20, 30}, message = "illegal status value")
    private Integer status;

}
