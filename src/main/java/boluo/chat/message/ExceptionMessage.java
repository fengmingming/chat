package boluo.chat.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExceptionMessage extends Message {

    private String exception;

    @Override
    public String getMsgType() {
        return "exception";
    }

}
