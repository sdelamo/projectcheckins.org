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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.api.Respondent;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Serdeable
public record QuestionRecord(
        @NotBlank(groups = Saved.class) String id,
        @NotBlank String title,
        @NotNull HowOften howOften,
        @NotEmpty Set<DayOfWeek> days,
        @NotNull TimeOfDay timeOfDay,
        @NotNull LocalTime fixedTime,
        @NotEmpty Set<? extends Respondent> respondents
) implements Question {
}
