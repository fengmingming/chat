package boluo.chat.message;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomMessage extends Message {

    @NotNull(message = "data is null")
    private Object data;

}
