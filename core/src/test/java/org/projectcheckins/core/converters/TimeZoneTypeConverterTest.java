package org.projectcheckins.core.converters;

import io.micronaut.core.convert.TypeConverter;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimeZoneTypeConverterTest {

    @Test
    void convert() {
        TypeConverter<String, TimeZone> converter = new TimeZoneTypeConverter();
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        assertThat(converter.convert("America/Los_Angeles", TimeZone.class, null))
                .hasValue(tz);
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        assertThat(converter.convert("Foo", TimeZone.class, null))
                .hasValue(gmt);
    }
}
