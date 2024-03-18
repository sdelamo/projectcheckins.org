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
class DayOfWeekRadiosTest {
    @Test
    void viewRender(ViewsRenderer<Map<String, Object>, HttpRequest<?>> viewsRenderer) {
        Writable writable = viewsRenderer.render("question/_dayOfWeekRadios.html",  Map.of(
                "prefix", "onceAWeek",
                "name", "onceAWeekDay",
                "value", DayOfWeek.TUESDAY
        ), null);

        assertThat(WritableUtils.writableToString(writable))
                .hasValueSatisfying(h -> assertThat(h)
                        .isEqualToIgnoringWhitespace("""
    <div id="onceAWeekDayPicker" class="d-none"
    data-questionform-target="onceAWeekDayPicker">
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" id="onceAWeekMonday"
               name="onceAWeekDay"
               value="MONDAY">
        <label class="form-check-label" for="onceAWeekMonday">Monday</label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" id="onceAWeekTuesday"
               name="onceAWeekDay"
               value="TUESDAY" checked="checked">
        <label class="form-check-label" for="onceAWeekTuesday">Tuesday</label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" id="onceAWeekWednesday"
               name="onceAWeekDay"
               value="WEDNESDAY">
        <label class="form-check-label" for="onceAWeekWednesday">Wednesday</label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" id="onceAWeekThursday"
               name="onceAWeekDay"
               value="THURSDAY">
        <label class="form-check-label" for="onceAWeekThursday">Thursday</label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" id="onceAWeekFriday"
               name="onceAWeekDay"
               value="FRIDAY">
        <label class="form-check-label" for="onceAWeekFriday">Friday</label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" id="onceAWeekSaturday"
               name="onceAWeekDay"
               value="SATURDAY">
        <label class="form-check-label" for="onceAWeekSaturday">Saturday</label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" id="onceAWeekSunday"
               name="onceAWeekDay"
               value="SUNDAY">
        <label class="form-check-label" for="onceAWeekSunday">Sunday</label>
    </div>
</div>"""));
    }
}
