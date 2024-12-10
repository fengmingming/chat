package boluo.chat.rest.account;

import boluo.chat.common.validation.IntEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindFriendApplyFormsReq {

    // 1我的申请 2我接收的申请
    @NotNull
    @IntEnum(value = {1, 2}, message = "illegal type")
    private Integer type;

}
