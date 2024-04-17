package org.projectcheckins.http.viewmodelprocessors;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@FunctionalInterface
public interface CustomDateTimeFormatter {
    @NonNull
    String format(@NonNull @NotNull LocalDate dateTime);
}
