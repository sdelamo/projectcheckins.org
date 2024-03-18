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
import org.projectcheckins.core.api.QuestionSave;
import org.projectcheckins.core.api.QuestionUpdate;
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
    public String save(@NotNull @Valid QuestionSave questionSave, @Nullable Tenant tenant) {
        QuestionEntity entity = new QuestionEntity();
        String id = idGenerator.generate();
        entity.id(id);
        entity.title(questionSave.title());
        entity.howOften(questionSave.howOften());
        entity.days(questionSave.days());
        entity.timeOfDay(questionSave.timeOfDay());
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
    public void update(@NotNull @Valid QuestionUpdate questionUpdate, @Nullable Tenant tenant) {
        QuestionEntity question = findEntityById(questionUpdate.id()).orElseThrow(QuestionNotFoundException::new);
        question.title(questionUpdate.title());
        question.howOften(questionUpdate.howOften());
        question.days(questionUpdate.days());
        question.timeOfDay(questionUpdate.timeOfDay());
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
