package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.repositories.ProfileRepository;

import java.util.Map;
import java.util.function.Function;

@Singleton
class AnswerSaveFormGeneratorImpl implements AnswerSaveFormGenerator {
    private final ProfileRepository profileRepository;
    private final FormGenerator formGenerator;
    private final Map<Format, Class<?>> FORMAT_TO_CLASS = Map.of(
            Format.MARKDOWN, AnswerMarkdownSave.class,
            Format.WYSIWYG, AnswerWysiwygSave.class
    );

    AnswerSaveFormGeneratorImpl(ProfileRepository profileRepository,
                                FormGenerator formGenerator) {
        this.profileRepository = profileRepository;
        this.formGenerator = formGenerator;
    }

    @Override
    @NonNull
    public Form generate(@NotNull Function<Format, String> actionFunction, @NotNull Authentication authentication) {
        Format preferedFormat = profileRepository.findByAuthentication(authentication).map(Profile::format).orElseThrow(UserNotFoundException::new);
        return formGenerator.generate(actionFunction.apply(preferedFormat), FORMAT_TO_CLASS.get(preferedFormat));
    }

}
