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

package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.annotations.Select;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

@Serdeable
public record ProfileUpdate(@NotNull @Select(fetcher = TimeZoneFetcher.class) TimeZone timeZone,
                            @NotNull DayOfWeek firstDayOfWeek,
                            @NotNull LocalTime beginningOfDay,
                            @NotNull LocalTime endOfDay,
                            @NotNull TimeFormat timeFormat,
                            @NotNull Format format,
                            @Nullable String firstName,
                            @Nullable String lastName) {
}
