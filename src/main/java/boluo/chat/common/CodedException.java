package boluo.chat.common;

import lombok.Getter;

@Getter
public class CodedException extends RuntimeException{

    private int code;

    public CodedException(int code, String message) {
        super(message);
    }

}
