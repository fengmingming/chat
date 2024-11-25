package boluo.chat.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ControlMessage extends Message {

    @NotBlank(message = "command is blank")
    private String command;
    private List<Object> args;

}
