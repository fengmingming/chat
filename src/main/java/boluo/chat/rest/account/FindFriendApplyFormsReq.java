package boluo.chat.rest.account;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindFriendApplyFormsReq {

    // 1我的申请 2我接收的申请
    @Pattern(regexp = "^[12]$", message = "illegal type")
    private int type = 1;

}
