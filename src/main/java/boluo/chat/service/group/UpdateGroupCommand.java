package boluo.chat.service.group;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateGroupCommand {

    @Size(max = 100, message = "groupName less than 100")
    private String groupName;

}
