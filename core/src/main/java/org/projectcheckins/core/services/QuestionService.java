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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.QuestionForm;
import org.projectcheckins.security.api.PublicProfile;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    @NonNull
    String save(@NotNull @Valid QuestionForm form, @Nullable Tenant tenant);

    @NonNull
    Optional<? extends Question> findById(@NotBlank String id, @Nullable Tenant tenant);

    void update(@NotBlank String id, @NotNull @Valid QuestionForm form, @Nullable Tenant tenant);

    @NonNull
    List<? extends Question> findAll(@Nullable Tenant tenant);

    void deleteById(@NotBlank String id, @Nullable Tenant tenant);

    @NonNull
    List<? extends PublicProfile> listAvailableRespondents(@Nullable Tenant tenant);
}
