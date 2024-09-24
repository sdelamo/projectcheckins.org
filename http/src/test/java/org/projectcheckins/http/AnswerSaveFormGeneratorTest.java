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

package org.projectcheckins.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.ViewsRenderer;
import io.micronaut.views.fields.Form;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.AnswerSaveFormGenerator;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileRecord;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;
import org.projectcheckins.test.WritableUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "AnswerSaveFormGeneratorTest")
class AnswerSaveFormGeneratorTest {

    private static final String USER_ID_MARKDOWN = "xxx";
    private static final String USER_ID_WYSIWYG = "yyy";

    @Test
    void renderForm(ViewsRenderer<Map<String, Object>, HttpRequest<?>> viewsRenderer,
                    AnswerSaveFormGenerator formGenerator) {
        Function<Format, String> formatFunction = format -> switch (format) {
            case MARKDOWN -> "/answer/save/markdown";
            case WYSIWYG -> "/answer/save/wysiwyg";
        };
        String questionId = "xxx";
        Authentication authentication = Authentication.build(USER_ID_MARKDOWN);
        assertThat(renderForm(questionId, formatFunction, authentication, formGenerator, viewsRenderer))
                .hasValueSatisfying(h -> assertThat(h)
                        .contains("action=\"/answer/save/markdown\"")
                        .contains("type=\"hidden\" name=\"questionId\"")
                        .contains("input type=\"date\" name=\"answerDate\"")
                        .contains("textarea name=\"markdown\"")
                );

        authentication = Authentication.build(USER_ID_WYSIWYG);
        assertThat(renderForm(questionId, formatFunction, authentication, formGenerator, viewsRenderer))
                .hasValueSatisfying(h -> assertThat(h)
                        .contains("action=\"/answer/save/wysiwyg\"")
                        .contains("type=\"hidden\" name=\"questionId\"")
                        .contains("input type=\"date\" name=\"answerDate\" value=\"" + LocalDate.now() + "\"")
                        .contains("type=\"hidden\" name=\"html\"")
                        .contains("<trix-editor class=\"form-control\" input=\"html\"></trix-editor>")
                );
    }

    private static Optional<String> renderForm(String questionId,
                                               Function<Format, String> formatFunction,
                                               Authentication authentication,
                                               AnswerSaveFormGenerator formGenerator,
                              ViewsRenderer<Map<String, Object>, HttpRequest<?>> viewsRenderer) {
        Form form = formGenerator.generate(questionId, formatFunction, authentication);
        return renderForm(form, viewsRenderer);
    }

    private static Optional<String> renderForm(Form form,
                                               ViewsRenderer<Map<String, Object>, HttpRequest<?>> viewsRenderer) {
        Writable writable = viewsRenderer.render("fieldset/form.html", Collections.singletonMap("form", form), null);
        return WritableUtils.writableToString(writable);
    }

    @Requires(property = "spec.name", value = "AnswerSaveFormGeneratorTest")
    @Singleton
    @Replaces(SecondaryProfileRepository.class)
    static class ProfileRepositoryReplacement extends SecondaryProfileRepository {

        @Override
        public Optional<? extends Profile> findById(String id, Tenant tenant) {
            if (id.equals(USER_ID_MARKDOWN)) {
                return Optional.of(createProfile(Format.MARKDOWN));
            } else if (id.equals(USER_ID_WYSIWYG)) {
                return Optional.of(createProfile(Format.WYSIWYG));
            }
            return Optional.empty();
        }

        @NonNull
        private Profile createProfile(@NonNull Format format) {
            return new ProfileRecord("id", "delamos@unityfoundation.io",
                    TimeZone.getDefault(),
                    DayOfWeek.MONDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(16, 30),
                    TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
                    format,
                    null,
                    null,
                    true
            );
        }
    }
}
