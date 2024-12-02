package boluo.chat.rest.group;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LeaveGroupCommand {

    @NotNull
    @Size(min = 1, max = 100)
    private List<String> accounts;

}
