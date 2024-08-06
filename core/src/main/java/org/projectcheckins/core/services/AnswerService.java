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

package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.AnswerView;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.models.DateAnswers;
import org.projectcheckins.core.forms.AnswerUpdate;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface AnswerService {

    @NonNull
    String save(@NotNull Authentication authentication, @NotNull @Valid AnswerSave answerSave, @Nullable Tenant tenant);

    void update(@NotNull Authentication authentication, @NotBlank String questionId, @NotBlank String id, @NotNull @Valid AnswerUpdate answerUpdate, @Nullable Tenant tenant);

    @NonNull
    Optional<? extends AnswerView> findById(@NotBlank String id, @NotNull Authentication authentication, @Nullable Tenant tenant);

    @NonNull
    List<? extends AnswerView> findByQuestionId(@NotBlank String questionId, @NotNull Authentication authentication, @Nullable Tenant tenant);

    @NonNull
    List<DateAnswers> findByQuestionIdGroupedByDate(@NotBlank String questionId, @NotNull Authentication authentication, @Nullable Tenant tenant);

    @NonNull
    String getAnswerSummary(@NotNull AnswerView answerView, @Nullable Locale locale);
}
