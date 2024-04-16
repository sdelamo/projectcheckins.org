package org.projectcheckins.security.http;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NewPasswordsMustMatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewPasswordsMustMatch {

    String MESSAGE = "org.projectcheckins.security.http.NewPasswordsMustMatch.message";

    String message() default "{" + MESSAGE + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
