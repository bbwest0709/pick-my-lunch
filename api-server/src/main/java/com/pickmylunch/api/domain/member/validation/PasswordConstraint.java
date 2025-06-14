package com.pickmylunch.api.domain.member.validation;

import jakarta.validation.*;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MemberValidator.PasswordValidator.class)
public @interface PasswordConstraint {
    String message() default "비밀번호는 영문자, 숫자를 포함해 10~20자리여야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
