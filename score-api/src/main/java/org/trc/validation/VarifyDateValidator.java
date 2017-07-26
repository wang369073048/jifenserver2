package org.trc.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/25
 */
public class VarifyDateValidator implements ConstraintValidator<VerifyDate, Long> {
    @Override
    public void initialize(VerifyDate verifyDate) {

    }

    @Override
    public boolean isValid(Long s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return false;
        }
        return true;
    }
}
