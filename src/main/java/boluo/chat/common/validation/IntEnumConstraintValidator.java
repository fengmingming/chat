package boluo.chat.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class IntEnumConstraintValidator implements ConstraintValidator<IntEnum, Integer> {

    private int[] value;

    public void initialize(IntEnum intEnum) {
        value = intEnum.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null) return true;
        return Arrays.stream(this.value).anyMatch(it -> it == value);
    }

}
