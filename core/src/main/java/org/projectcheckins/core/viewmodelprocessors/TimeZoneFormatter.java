package org.projectcheckins.core.viewmodelprocessors;

import io.micronaut.core.annotation.NonNull;

import java.util.TimeZone;

public interface TimeZoneFormatter {

    @NonNull
    String format(@NonNull TimeZone timeZone);
}
