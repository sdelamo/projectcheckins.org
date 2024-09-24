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
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.forms.QuestionForm;
import org.projectcheckins.core.forms.QuestionFormRecord;
import org.projectcheckins.core.forms.QuestionRecord;
import org.projectcheckins.core.forms.RespondentRecord;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.security.api.PublicProfile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;

@Singleton
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ProfileRepository profileRepository;
    private final SchedulingService schedulingService;

    public QuestionServiceImpl(QuestionRepository questionRepository, ProfileRepository profileRepository, SchedulingService schedulingService) {
        this.questionRepository = questionRepository;
        this.profileRepository = profileRepository;
        this.schedulingService = schedulingService;
    }

    @Override
    @NonNull
    public String save(@NotNull @Valid QuestionForm form, @Nullable Tenant tenant) {
        return questionRepository.save(question(null, form, tenant), tenant);
    }

    @Override
    @NonNull
    public Optional<? extends Question> findById(@NotBlank String id, @Nullable Tenant tenant) {
        return questionRepository.findById(id, tenant);
    }

    @Override
    public void update(@NotBlank String id, @NotNull @Valid QuestionForm form, @Nullable Tenant tenant) {
        questionRepository.update(question(id, form, tenant), tenant);
    }

    @Override
    @NonNull
    public List<? extends Question> findAll(@Nullable Tenant tenant) {
        return questionRepository.findAll(tenant);
    }

    @Override
    public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {
        questionRepository.deleteById(id, tenant);
    }

    @Override
    @NonNull
    public List<? extends PublicProfile> listAvailableRespondents(@Nullable Tenant tenant) {
        return profileRepository.list(tenant);
    }

    private QuestionRecord question(@Nullable String questionId, @NotNull QuestionForm form, @Nullable Tenant tenant) {
        return Optional.ofNullable(questionId)
                .flatMap(id -> findById(id, tenant))
                .filter(q -> doesNotNeedRecalculation(q, form))
                .map(q -> updatedQuestion(q, form))
                .orElseGet(() -> newQuestion(questionId, form, tenant));
    }

    private boolean doesNotNeedRecalculation(@NotNull Question question, @NotNull QuestionForm form) {
        return formHash(form) == formHash(QuestionFormRecord.of(question));
    }

    private int formHash(@NotNull QuestionForm form) {
        return Objects.hash(form.howOften(), form.days().stream().sorted().toList(), form.timeOfDay(), form.fixedTime(), form.respondentIds().stream().sorted().toList());
    }

    private QuestionRecord updatedQuestion(@NotNull Question same, @NotNull QuestionForm form) {
        return new QuestionRecord(
                same.id(),
                form.title(),
                same.howOften(),
                same.days(),
                same.timeOfDay(),
                same.fixedTime(),
                same.respondents());
    }

    private QuestionRecord newQuestion(@Nullable String questionId, @NotNull QuestionForm form, @Nullable Tenant tenant) {
        return new QuestionRecord(
                questionId,
                form.title(),
                form.howOften(),
                form.days(),
                form.timeOfDay(),
                form.fixedTime(),
                Optional.ofNullable(form.respondentIds()).orElseGet(Collections::emptySet).stream()
                        .map(id -> createRespondent(id, form, tenant)).collect(toSet()));
    }

    private RespondentRecord createRespondent(@NotBlank String profileId, @NonNull QuestionForm form, @Nullable Tenant tenant) {
        final Profile profile = profileRepository.findById(profileId, tenant).orElseThrow(UserNotFoundException::new);
        return new RespondentRecord(
                profileId,
                schedulingService.nextExecution(
                        null,
                        form.howOften(),
                        form.days(),
                        profile.timeZone().toZoneId(),
                        form.timeOfDay().getTime(form.fixedTime(), profile)
                ));
    }
}
