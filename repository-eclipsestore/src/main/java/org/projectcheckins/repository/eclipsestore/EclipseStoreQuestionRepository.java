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
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.exceptions.QuestionNotFoundException;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.core.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Singleton
class EclipseStoreQuestionRepository implements QuestionRepository {
    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;

    public EclipseStoreQuestionRepository(RootProvider<Data> rootProvider,
                                          IdGenerator idGenerator) {
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
    }

    @Override
    @NonNull
    public String save(@NotNull @Valid Question questionSave, @Nullable Tenant tenant) {
        QuestionEntity entity = new QuestionEntity();
        String id = idGenerator.generate();
        entity.id(id);
        entity.title(questionSave.title());
        entity.howOften(questionSave.howOften());
        entity.days(questionSave.days());
        entity.timeOfDay(questionSave.timeOfDay());
        entity.fixedTime(questionSave.fixedTime());
        entity.respondents(questionSave.respondents());
        rootProvider.root().getQuestions().add(entity);
        save(rootProvider.root().getQuestions());
        return id;
    }

    @Override
    @NonNull
    public Optional<QuestionEntity> findById(@NotBlank String id, @Nullable Tenant tenant) {
        return findEntityById(id);
    }

    @Override
    public void update(@NotNull @Valid Question questionUpdate, @Nullable Tenant tenant) {
        QuestionEntity question = findEntityById(questionUpdate.id()).orElseThrow(QuestionNotFoundException::new);
        question.title(questionUpdate.title());
        question.howOften(questionUpdate.howOften());
        question.days(questionUpdate.days());
        question.timeOfDay(questionUpdate.timeOfDay());
        question.fixedTime(questionUpdate.fixedTime());
        question.respondents(questionUpdate.respondents());
        save(question);
    }

    @Override
    public List<QuestionEntity> findAll(@Nullable Tenant tenant) {
        return rootProvider.root().getQuestions().stream().toList();
    }

    @Override
    public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {
        rootProvider.root().getQuestions().removeIf(q -> q.id().equals(id));
        save(rootProvider.root().getQuestions());
    }

    public Optional<QuestionEntity> findEntityById(String id) {
        return rootProvider.root().getQuestions()
                .stream()
                .filter(q -> q.id().equals(id))
                .findFirst();
    }

    @StoreParams("questions")
    public void save(List<QuestionEntity> questions) {
    }

    @StoreParams("question")
    public void save(QuestionEntity question) {
    }
}
