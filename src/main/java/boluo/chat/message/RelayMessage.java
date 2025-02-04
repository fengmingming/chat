package boluo.chat.message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RelayMessage extends Message {

    @Valid
    @Size(min = 1)
    private List<Message> relayMessages;

    @Override
    public String getMsgType() {
        return "relay";
    }

}
