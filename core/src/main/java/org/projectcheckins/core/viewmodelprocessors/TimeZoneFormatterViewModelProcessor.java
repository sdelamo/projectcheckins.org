package org.projectcheckins.core.viewmodelprocessors;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;
import java.util.Map;

@Singleton
class TimeZoneFormatterViewModelProcessor extends MapViewModelProcessor {
    public static final String MODEL_TIME_ZONE_FORMATTER = "timeZoneFormatter";
    private final TimeZoneFormatter timeZoneFormatter;

    TimeZoneFormatterViewModelProcessor(TimeZoneFormatter timeZoneFormatter) {
        this.timeZoneFormatter = timeZoneFormatter;
    }

    @Override
    protected void populateModel(@NonNull HttpRequest<?> request, @NonNull Map<String, Object> viewModel) {
        viewModel.putIfAbsent(MODEL_TIME_ZONE_FORMATTER, timeZoneFormatter);
    }
}
