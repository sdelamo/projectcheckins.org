package org.projectcheckins.core.configuration;

import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.TimeFormat;

import java.time.DayOfWeek;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileConfigurationPropertiesTest {

    @Test
    void settersAndGeters() {
        assertThat(new ProfileConfigurationProperties())
                .hasFieldOrPropertyWithValue("timeZone", TimeZone.getDefault())
                .hasFieldOrPropertyWithValue("firstDayOfWeek", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("timeFormat", TimeFormat.TWENTY_FOUR_HOUR_CLOCK)
                .hasFieldOrPropertyWithValue("format", Format.MARKDOWN);
        var conf = new ProfileConfigurationProperties();
        conf.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        conf.setTimeFormat(TimeFormat.TWELVE_HOUR_CLOCK);
        var angeles = TimeZone.getTimeZone("America/Los_Angeles");
        conf.setTimeZone(angeles);
        conf.setFormat(Format.WYSIWYG);
        assertThat(conf)
                .hasFieldOrPropertyWithValue("timeZone", angeles)
                .hasFieldOrPropertyWithValue("firstDayOfWeek", DayOfWeek.SUNDAY)
                .hasFieldOrPropertyWithValue("timeFormat", TimeFormat.TWELVE_HOUR_CLOCK)
                .hasFieldOrPropertyWithValue("format", Format.WYSIWYG);
    }
}