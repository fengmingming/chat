package boluo.chat.domain;

import lombok.Getter;

@Getter
public enum AccountApplyFormStatusEnum {

    Applied(10, "已申请"),
    Agreed(20, "已同意"),
    Rejected(30, "已拒绝"),
    ;

    private final int code;
    private final String codeDesc;

    AccountApplyFormStatusEnum(int code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    public static AccountApplyFormStatusEnum findByCode(int code) {
        for(AccountApplyFormStatusEnum e: AccountApplyFormStatusEnum.values()) {
            if(e.getCode() == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("illegal status " + code);
    }

}
