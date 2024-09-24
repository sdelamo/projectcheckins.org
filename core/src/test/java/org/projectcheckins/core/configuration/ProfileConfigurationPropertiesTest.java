// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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