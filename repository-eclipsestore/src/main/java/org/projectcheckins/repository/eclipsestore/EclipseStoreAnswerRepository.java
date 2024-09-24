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

package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.validation.Validated;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.exceptions.AnswerNotFoundException;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.forms.Saved;

import static java.util.Comparator.comparing;

@Singleton
class EclipseStoreAnswerRepository implements AnswerRepository {

    private static final Comparator<AnswerEntity> CHRONOLOGICALLY = comparing(AnswerEntity::answerDate);

    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;

    public EclipseStoreAnswerRepository(RootProvider<Data> rootProvider,
                                        IdGenerator idGenerator) {
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
    }

    @Override
    @NonNull
    public String save(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
        String id = idGenerator.generate();
        AnswerEntity entity = answerOf(id, answer);
        save(rootProvider.root().getAnswers(), entity);
        return id;
    }

    @Validated(groups = Saved.class)
    @Override
    public void update(@NotNull @Valid Answer answerUpdate, @Nullable Tenant tenant) {
        final AnswerEntity answer = findByQuestionIdAndRespondentId(answerUpdate.questionId(), answerUpdate.respondentId())
                .stream().filter(a -> a.id().equals(answerUpdate.id())).findAny()
                .orElseThrow(AnswerNotFoundException::new);
        answer.answerDate(answerUpdate.answerDate());
        answer.format(answerUpdate.format());
        answer.text(answerUpdate.text());
        save(answer);
    }

    @Override
    @NonNull
    public Optional<AnswerEntity> findById(@NotBlank String id, @Nullable Tenant tenant) {
        return rootProvider.root().getAnswers().stream()
                .filter(a -> a.id().equals(id))
                .findAny();
    }

    @Override
    @NonNull
    public List<AnswerEntity> findByQuestionId(@NotBlank String questionId,
                                                   @Nullable Tenant tenant) {
        return rootProvider.root().getAnswers().stream()
                .filter(a -> a.questionId().equals(questionId))
                .sorted(CHRONOLOGICALLY.reversed())
                .toList();
    }

    @Override
    @NonNull
    public List<AnswerEntity> findByQuestionIdAndRespondentId(
            @NotBlank String questionId,
            @NotNull String respondentId) {
        return rootProvider.root().getAnswers().stream()
                .filter(a -> a.questionId().equals(questionId) && a.respondentId().equals(respondentId))
                .sorted(CHRONOLOGICALLY.reversed())
                .toList();
    }

    @StoreParams("answers")
    public void save(List<AnswerEntity> answers, AnswerEntity answer) {
        answers.add(answer);
    }

    @StoreParams("answer")
    public void save(AnswerEntity answer) {
    }

    @NonNull
    private static AnswerEntity answerOf(@NonNull String id, @NonNull Answer answer) {
        AnswerEntity entity = new AnswerEntity();
        entity.id(id);
        entity.questionId(answer.questionId());
        entity.respondentId(answer.respondentId());
        entity.answerDate(answer.answerDate());
        entity.format(answer.format());
        entity.text(answer.text());
        return entity;
    }
}
