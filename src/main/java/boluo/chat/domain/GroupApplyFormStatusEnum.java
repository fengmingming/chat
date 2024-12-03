package boluo.chat.domain;

import lombok.Getter;

@Getter
public enum GroupApplyFormStatusEnum {

    Applied(10, "已申请"),
    Agreed(20, "已同意"),
    Rejected(30, "已拒绝"),
    ;

    private final int code;
    private final String codeDesc;

    GroupApplyFormStatusEnum(int code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    public static GroupApplyFormStatusEnum findByCode(int code) {
        for(GroupApplyFormStatusEnum e:GroupApplyFormStatusEnum.values()) {
            if(e.getCode() == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("illegal status " + code);
    }

}
