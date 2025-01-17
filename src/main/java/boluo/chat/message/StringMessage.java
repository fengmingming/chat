package boluo.chat.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StringMessage extends Message {

    @NotBlank(message = "content is blank")
    private String content;

    @Override
    public String getMsgType() {
        return "text";
    }

}
