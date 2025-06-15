package com.pickmylunch.api.domain.member.validation;

import jakarta.validation.*;

public class MemberValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{10,20}$";

    public static class EmailValidator implements ConstraintValidator<EmailConstraint, String> {
        @Override
        public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
            return email != null && email.matches(EMAIL_REGEX);
        }
    }

    public static class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {
        @Override
        public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
            return password != null && password.matches(PASSWORD_REGEX);
        }
    }
}
