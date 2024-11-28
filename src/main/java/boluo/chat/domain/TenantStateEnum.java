package boluo.chat.domain;

import lombok.Getter;

@Getter
public enum TenantStateEnum {

    INIT(10, "已注册");

    private final int code;
    private final String codeDesc;

    TenantStateEnum(int code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

}
