package boluo.chat.rest.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindGroupApplyFormsReq {

    @NotBlank(message = "account is blank")
    private String account;
    private Integer status;

}
