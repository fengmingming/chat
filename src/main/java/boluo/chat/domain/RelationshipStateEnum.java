package boluo.chat.domain;

import lombok.Getter;

@Getter
public enum RelationshipStateEnum {

    NORMAL(10, "正常");

    private final int code;
    private final String codeDesc;

    RelationshipStateEnum(int code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

}
