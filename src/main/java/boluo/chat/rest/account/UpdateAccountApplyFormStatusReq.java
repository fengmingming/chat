package boluo.chat.rest.account;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAccountApplyFormStatusReq {

    @NotNull
    private Integer status;

}
