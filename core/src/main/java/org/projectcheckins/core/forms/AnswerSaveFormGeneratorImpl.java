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
        Format preferedFormat = getFormatByAuthentication(authentication);
        Object instance = switch (preferedFormat) {
            case WYSIWYG -> new AnswerWysiwygSave(questionId, authentication.getName(), LocalDate.now(), null);
            case MARKDOWN -> new AnswerMarkdownSave(questionId, authentication.getName(), LocalDate.now(), null);
        };
        return formGenerator.generate(actionFunction.apply(preferedFormat), instance);
    }

    private Format getFormatByAuthentication(Authentication authentication) {
        return profileRepository.findByAuthentication(authentication)
                .map(Profile::format)
                .orElseThrow(UserNotFoundException::new);
    }

}
