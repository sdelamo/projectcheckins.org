package org.projectcheckins.core.viewmodelprocessors;

import jakarta.inject.Singleton;

import java.util.TimeZone;

@Singleton
class DefaultTimeZoneFormatter implements TimeZoneFormatter {

    @Override
    public String format(TimeZone timeZone) {
        return timeZone.getID() + " (" + timeZone.getDisplayName() + ")";
    }
}
