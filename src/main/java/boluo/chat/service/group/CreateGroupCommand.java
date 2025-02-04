package boluo.chat.service.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CreateGroupCommand {

    @NotBlank(message = "groupId is blank")
    @Size(max = 100)
    private String groupId;
    @Size(max = 100)
    private String groupName;
    @NotBlank(message = "managerAccount is blank")
    private String managerAccount;
    @Size(max = 100)
    private List<String> accounts;

}
