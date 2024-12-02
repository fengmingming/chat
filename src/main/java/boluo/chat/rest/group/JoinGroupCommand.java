package boluo.chat.rest.group;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JoinGroupCommand {

    @NotNull
    @Size(min = 1)
    private List<String> accounts;

}
