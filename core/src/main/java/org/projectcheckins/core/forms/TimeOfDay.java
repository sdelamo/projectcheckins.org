package org.projectcheckins.core.forms;

import org.projectcheckins.core.api.Profile;

import java.time.LocalTime;
import java.util.function.BiFunction;

public enum TimeOfDay {
    BEGINNING((t, p) -> p.beginningOfDay()),
    END((t, p) -> p.endOfDay()),
    FIXED((t, p) -> t);

    private final BiFunction<LocalTime, Profile, LocalTime> function;

    TimeOfDay(BiFunction<LocalTime, Profile, LocalTime> function) {
        this.function = function;
    }

    public LocalTime getTime(LocalTime fixedTime, Profile profile) {
        return function.apply(fixedTime, profile);
    }
}
