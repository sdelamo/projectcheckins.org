package org.projectcheckins.security.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidTokenValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidToken {

    String MESSAGE = "org.projectcheckins.security.constraints.ValidToken.message";

    String message() default "{" + MESSAGE + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
