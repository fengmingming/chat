package boluo.chat.common;

import lombok.Getter;

@Getter
public class CodedException extends RuntimeException{

    private final int code;

    public CodedException(int code, String message) {
        super(message);
        this.code = code;
    }

}
