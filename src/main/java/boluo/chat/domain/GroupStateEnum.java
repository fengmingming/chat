package boluo.chat.domain;

import lombok.Getter;

@Getter
public enum GroupStateEnum {

    Created(10, "已创建"),
    disband(99, "解散"),
    ;

    private final int code;
    private final String codeDesc;

    GroupStateEnum(int code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

}
