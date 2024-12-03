package boluo.chat.common;

public class Can {

    private final boolean result;
    private ResVo<?> error;

    public Can(boolean result) {
        this.result = result;
    }

    public Can(ResVo<?> error) {
        this(false);
        this.error = error;
    }

    public ResVo<?> error() {
        return this.error;
    }

    public boolean get() {
        return result;
    }

}
