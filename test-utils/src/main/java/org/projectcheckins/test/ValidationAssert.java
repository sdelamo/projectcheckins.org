package org.projectcheckins.test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Set;

public class ValidationAssert<T> extends AbstractAssert<ValidationAssert<T>, Set<ConstraintViolation<T>>> {

    public ValidationAssert(Set<ConstraintViolation<T>> violations) {
        super(violations, ValidationAssert.class);
    }

    public static <T> ValidationAssert<T> assertThat(Set<ConstraintViolation<T>> violations) {
        return new ValidationAssert<>(violations);
    }

    public ValidationAssert <T> fieldNotBlank(String name) {
        expectedViolationMessage(name, "must not be blank");
        return this;
    }

    public ValidationAssert<T> isValid() {
        Assertions.assertThat(actual).isEmpty();
        return this;
    }

    public ValidationAssert<T> hasNotNullViolation(String name) {
        expectedViolationMessage(name, "must not be null");
        return this;
    }

    private void expectedViolationMessage(String name, String message) {
        Assertions.assertThat(actual)
                .anyMatch(x -> x.getPropertyPath().toString().equals(name))
                .extracting(ConstraintViolation::getMessage)
                .anyMatch(message::equals);
    }

    public ValidationAssert<T> hasMalformedEmailViolation(String name) {
        expectedViolationMessage(name, "must be a well-formed email address");
        return this;
    }
}
