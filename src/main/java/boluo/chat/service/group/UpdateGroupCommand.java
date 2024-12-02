package boluo.chat.service.group;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateGroupCommand {

    @Max(value = 100, message = "groupName less than 100")
    private String groupName;

}
