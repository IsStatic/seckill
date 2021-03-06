package com.example.seckilldemo.vo;

import com.example.seckilldemo.utils.ValidatorUtil;
import com.example.seckilldemo.validator.isMobile;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*
* 手机号码校验规则
* */
public class isMobileValidator implements ConstraintValidator<isMobile,String> {

    private boolean required = false;

    @Override
    public void initialize(isMobile constraintAnnotation) {
         required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(s);
        }else{
            if(StringUtils.isEmpty(s)){
                return true;
            }else{
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
