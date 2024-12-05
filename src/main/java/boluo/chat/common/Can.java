package boluo.chat.common;

/**
 * 业务逻辑判断结果
 * */
public class Can {

    private final boolean result;
    private Integer code;
    private String codeDesc;

    public Can(boolean result) {
        this.result = result;
    }

    public Can(int code, String codeDesc) {
        this(false);
        this.code = code;
        this.codeDesc = codeDesc;
    }

    public ResVo<?> error() {
        if(result) return null;
        return ResVo.error(code, codeDesc);
    }

    public boolean get() {
        return result;
    }

    public CodedException exception() {
        if(result) return null;
        return new CodedException(code, codeDesc);
    }

    public void throwException() {
        if(!result) {
            throw exception();
        }
    }

}
