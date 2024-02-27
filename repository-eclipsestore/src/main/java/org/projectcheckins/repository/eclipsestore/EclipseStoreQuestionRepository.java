package org.projectcheckins.repository.eclipsestore;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
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
    public String save(QuestionSave questionSave) {
        QuestionEntity entity = new QuestionEntity();
        String id = idGenerator.generate();
        entity.setId(id);
        entity.setTitle(questionSave.title());
        rootProvider.root().getQuestions().add(entity);
        save(rootProvider.root().getQuestions());
        return id;
    }

    @Override
    public Optional<Question> findById(String id) {
        return findEntityById(id)
                .map(this::questionOfEntity);
    }

    private Question questionOfEntity(QuestionEntity entity) {
        return new Question(entity.getId(), entity.getTitle());
    }

    public Optional<QuestionEntity> findEntityById(String id) {
        return rootProvider.root().getQuestions()
                .stream()
                .filter(q -> q.getId().equals(id))
                .findFirst();
    }

    @Override
    public void update(QuestionUpdate questionUpdate) {
        QuestionEntity question = findEntityById(questionUpdate.id()).orElseThrow(QuestionNotFoundException::new);
        question.setTitle(questionUpdate.title());
        save(question);
    }

    @Override
    public List<Question> findAll() {
        return rootProvider.root().getQuestions().stream().map(this::questionOfEntity).toList();
    }

    @Override
    public void deleteById(@NotBlank String id) {
        rootProvider.root().getQuestions().removeIf(q -> q.getId().equals(id));
        save(rootProvider.root().getQuestions());
    }

    @StoreParams("questions")
    public void save(List<QuestionEntity> questions) {
    }

    @StoreParams("question")
    public void save(QuestionEntity question) {
    }
}
