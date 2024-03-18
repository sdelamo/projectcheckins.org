package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.views.fields.messages.ConstraintViolationUtils;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;

import java.util.*;

public final class ConstraintViolationExceptionUtils {

    private ConstraintViolationExceptionUtils() {

    }

    @NonNull
    public static ValidatedFieldset validatedFieldset(@Nullable ConstraintViolationException ex, @NonNull Class<?> clazz) {
        return new ValidatedFieldset() {
            @Override
            public Map<String, List<Message>> fieldErrors() {
                return messageOf(ex, BeanIntrospection.getIntrospection(clazz));
            }

            @Override
            public List<Message> errors() {
                return ex.getConstraintViolations()
                        .stream()
                        .filter(constraintViolationEx -> ConstraintViolationUtils.lastNode(constraintViolationEx).isEmpty())
                        .map(Message::of)
                        .sorted()
                        .toList();
            }
        };
    }

    @NonNull
    private static Map<String, List<Message>> messageOf(@NonNull ConstraintViolationException ex, @NonNull BeanIntrospection<?> beanIntrospection) {
        Map<String, List<Message>> errors = new HashMap<>();
        for (BeanProperty<?, ?> beanProperty : beanIntrospection.getBeanProperties()) {
            List<Message> messages = messagesForConstraintViolationExceptionAndBeanProperty(ex, beanProperty);
            if (CollectionUtils.isNotEmpty(messages)) {
                errors.put(beanProperty.getName(), messages);
            }
        }
        return errors;
    }

    @NonNull
    private static List<Message> messagesForConstraintViolationExceptionAndBeanProperty(@Nullable ConstraintViolationException ex,
                                                                                        @NonNull BeanProperty<?, ?> beanProperty) {
        return ex == null ? Collections.emptyList() : ex.getConstraintViolations().stream().filter((violation) -> {
                    Optional<String> lastNodeOptional = ConstraintViolationUtils.lastNode(violation);
                    return lastNodeOptional.isPresent() && (lastNodeOptional.get()).equals(beanProperty.getName());
                }).map(Message::of)
                .toList();
    }
}
