package boluo.chat.service.group;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CreateGroupCommand {

    @NotBlank(message = "groupId is blank")
    @Max(value = 100)
    private String groupId;
    @Max(value = 100)
    private String groupName;
    @Size(max = 100)
    private List<String> accounts;

}
