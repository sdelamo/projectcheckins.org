package org.projectcheckins.core.converters;

import io.micronaut.core.convert.MutableConversionService;
import io.micronaut.core.convert.TypeConverterRegistrar;

import java.util.TimeZone;

public class ProjectCheckinsTypeConverterRegistrar implements TypeConverterRegistrar {
    @Override
    public void register(MutableConversionService conversionService) {
        conversionService.addConverter(String.class, TimeZone.class, new TimeZoneTypeConverter());
    }
}
