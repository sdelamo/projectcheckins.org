package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.repositories.ProfileRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;

@Singleton
class AnswerSaveFormGeneratorImpl implements AnswerSaveFormGenerator {
    private final ProfileRepository profileRepository;
    private final FormGenerator formGenerator;

    AnswerSaveFormGeneratorImpl(ProfileRepository profileRepository,
                                FormGenerator formGenerator) {
        this.profileRepository = profileRepository;
        this.formGenerator = formGenerator;
    }

    @Override
    @NonNull
    public Form generate(@NotBlank String questionId, @NotNull Function<Format, String> actionFunction, @NotNull Authentication authentication) {
        Format preferedFormat = profileRepository.findByAuthentication(authentication).map(Profile::format).orElseThrow(UserNotFoundException::new);
        Object instance = switch (preferedFormat) {
            case WYSIWYG -> new AnswerWysiwygSave(questionId, authentication.getName(), LocalDate.now(), null);
            case MARKDOWN -> new AnswerMarkdownSave(questionId, authentication.getName(), LocalDate.now(), null);
        };
        return formGenerator.generate(actionFunction.apply(preferedFormat), instance);
    }

}
