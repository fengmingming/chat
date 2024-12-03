package boluo.chat.rest.group;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateGroupApplyFormStatusReq {

    @NotNull(message = "status is null")
    private Integer status;

}
