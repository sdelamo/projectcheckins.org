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
