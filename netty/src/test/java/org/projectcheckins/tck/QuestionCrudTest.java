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

package org.projectcheckins.tck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.security.*;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;

import java.util.*;

@MicronautTest
@Property(name = "spec.name", value = "QuestionCrudTest")
class QuestionCrudTest {
    @Requires(property = "spec.name", value = "QuestionCrudTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
    }

    @Test
    void questionCrud(@Client("/") HttpClient httpClient,
                      QuestionRepository questionRepository,
                      RegisterService registerService,
                      TeamInvitationRepository teamInvitationRepository,
                      AuthenticationFetcherMock authenticationFetcher) throws RegistrationCheckViolationException {
        String email = "delamos@unityfoundation.io";
        teamInvitationRepository.save(new TeamInvitationRecord(email, null));
        String userId = registerService.register(email, "secret", null);
        Authentication authentication = Authentication.build(userId, Collections.emptyList(), Collections.singletonMap("email", email));
        authenticationFetcher.setAuthentication(authentication);
        BlockingHttpClient client = httpClient.toBlocking();

        String title = "What are working on?";
        String body = "title="+title+"&howOften=DAILY_ON&dailyOnDay=MONDAY&dailyOnDay=TUESDAY&dailyOnDay=WEDNESDAY&dailyOnDay=THURSDAY&dailyOnDay=FRIDAY&timeOfDay=END&fixedTime=16:30&respondentIds=" + userId;
        HttpRequest<?> request = BrowserRequest.POST("/question/save", body);
        assertThatCode(() -> client.exchange(request))
            .doesNotThrowAnyException();
        Optional<? extends Question> questionOptional = questionRepository.findAll()
                .stream()
                .filter(q -> q.title().equals("What are working on?"))
                .findFirst();
        assertThat(questionOptional).isNotEmpty();
        questionOptional.map(Question::id).ifPresent(questionRepository::deleteById);
    }
}
