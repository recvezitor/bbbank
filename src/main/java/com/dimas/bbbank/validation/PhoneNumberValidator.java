package com.dimas.bbbank.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;


public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private final Pattern pattern = Pattern.compile("\\d{11}");//should be more complex regular expression

    @Override
    public void initialize(PhoneNumber arg0) {
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        return isNull(input) || pattern.matcher(input).matches();
    }
}

