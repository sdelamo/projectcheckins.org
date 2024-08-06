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

package org.projectcheckins.http.views;

import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.ViewsRenderer;
import org.junit.jupiter.api.Test;
import org.projectcheckins.test.WritableUtils;

import java.time.DayOfWeek;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MicronautTest(startApplication = false)
class DayOfWeekRadioTest {

    @Test
    void viewRender(ViewsRenderer<Map<String, Object>, HttpRequest<?>> viewsRenderer) {
        Writable writable = viewsRenderer.render("question/_dayOfWeekRadio.html",  Map.of(
                "prefix", "onceAWeek",
                "name", "onceAWeekDay",
                "value", DayOfWeek.TUESDAY,
                "dayPrefix", "Monday",
                "day", DayOfWeek.MONDAY.name(),
                "dayCopy", "dayofweek.monday"
        ), null);

        assertThat(WritableUtils.writableToString(writable))
                .hasValueSatisfying(h -> assertThat(h)
                        .isEqualToIgnoringWhitespace("""
                               <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="onceAWeekMonday"
                                           name="onceAWeekDay"
                                           value="MONDAY">
                                    <label class="form-check-label" for="onceAWeekMonday">Monday</label>
                                </div>"""));
    }


}
