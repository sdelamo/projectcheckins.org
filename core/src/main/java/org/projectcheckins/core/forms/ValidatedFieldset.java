package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.views.fields.messages.Message;

import java.util.List;
import java.util.Map;

public interface ValidatedFieldset {
    @Nullable Map<String, List<Message>> fieldErrors();
    @Nullable List<Message> errors();

    default boolean hasFieldsetErrors() {
        return CollectionUtils.isNotEmpty(fieldErrors());
    }

    default boolean hasError(@NonNull String fieldName) {
        return CollectionUtils.isNotEmpty(fieldErrors()) && fieldErrors().containsKey(fieldName);
    }

    @NonNull
    default List<Message> getError(@NonNull String fieldName) {
        return fieldErrors().get(fieldName);
    }

    default boolean hasErrors() {
        return CollectionUtils.isNotEmpty(errors());
    }
}
