package org.projectcheckins.core.converters;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import java.util.Optional;
import java.util.TimeZone;


public class TimeZoneTypeConverter implements TypeConverter<String, TimeZone>{
    @Override
    public Optional<TimeZone> convert(String timeZoneId, Class<TimeZone> targetType, ConversionContext context) {
        return Optional.of(TimeZone.getTimeZone(timeZoneId));
    }
}
