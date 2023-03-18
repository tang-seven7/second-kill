package com.seven.seckill.entities.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class MobileValidator implements ConstraintValidator<IsMobile,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return validateCode(value);
    }

    private boolean validateCode(String mobile) {
        if (mobile.length()!=11){
            return false;
        }
        Pattern mobile_pattern = Pattern.compile("[1][3,4,5,6,7,8,9][0-9]{9}$");
        return mobile_pattern.matcher(mobile).matches();
    }
}
