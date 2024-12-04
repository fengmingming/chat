package boluo.chat.service.account;

import boluo.chat.domain.AccountApplyFormStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAccountApplyFormStatusCommand {

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
    @NotNull(message = "accountApplyFormId is null")
    private Long accountApplyFormId;
    @NotNull(message = "status is null")
    private AccountApplyFormStatusEnum status;

}
