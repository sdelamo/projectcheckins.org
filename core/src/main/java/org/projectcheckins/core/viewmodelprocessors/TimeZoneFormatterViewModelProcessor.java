package org.projectcheckins.core.viewmodelprocessors;

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
    protected void populateModel(Map<String, Object> viewModel) {
        viewModel.putIfAbsent(MODEL_TIME_ZONE_FORMATTER, timeZoneFormatter);
    }
}
