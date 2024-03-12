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
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;
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
        entity.setId(id);
        entity.setTitle(questionSave.title());
        entity.setSchedule(questionSave.schedule());
        rootProvider.root().getQuestions().add(entity);
        save(rootProvider.root().getQuestions());
        return id;
    }

    @Override
    @NonNull
    public Optional<Question> findById(@NotBlank String id, @Nullable Tenant tenant) {
        return findEntityById(id)
                .map(this::questionOfEntity);
    }

    @Override
    public void update(@NotNull @Valid QuestionUpdate questionUpdate, @Nullable Tenant tenant) {
        QuestionEntity question = findEntityById(questionUpdate.id()).orElseThrow(QuestionNotFoundException::new);
        question.setTitle(questionUpdate.title());
        question.setSchedule(questionUpdate.schedule());
        save(question);
    }

    @Override
    public List<Question> findAll(@Nullable Tenant tenant) {
        return rootProvider.root().getQuestions().stream().map(this::questionOfEntity).toList();
    }

    @Override
    public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {
        rootProvider.root().getQuestions().removeIf(q -> q.getId().equals(id));
        save(rootProvider.root().getQuestions());
    }

    private Question questionOfEntity(QuestionEntity entity) {
        return new Question(
            entity.getId(),
            entity.getTitle(),
            entity.getSchedule());
    }

    public Optional<QuestionEntity> findEntityById(String id) {
        return rootProvider.root().getQuestions()
                .stream()
                .filter(q -> q.getId().equals(id))
                .findFirst();
    }

    @StoreParams("questions")
    public void save(List<QuestionEntity> questions) {
    }

    @StoreParams("question")
    public void save(QuestionEntity question) {
    }
}
