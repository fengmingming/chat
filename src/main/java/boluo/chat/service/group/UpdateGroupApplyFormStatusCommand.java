package boluo.chat.service.group;

import boluo.chat.domain.GroupApplyFormStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateGroupApplyFormStatusCommand {

    private Long tenantId;
    private String groupId;
    private Long groupApplyFormId;
    private GroupApplyFormStatusEnum status;
    private String account;

}
