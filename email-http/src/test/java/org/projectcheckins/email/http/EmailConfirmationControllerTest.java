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

package org.projectcheckins.email.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.email.EmailConfirmationTokenGenerator;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "EmailConfirmationControllerTest")
@MicronautTest
class EmailConfirmationControllerTest {
    @Test
    void successfulRedirection(@Client("/") HttpClient httpClient,
                               UserRepositoryMock userRepository,
                               EmailConfirmationTokenGenerator tokenGenerator) {
        BlockingHttpClient client = httpClient.toBlocking();
        String email = "delamos@unityfoundation.io";
        String token = tokenGenerator.generateToken(email);
        URI uri = UriBuilder.of("/email").path("confirm").queryParam("token", token).build();
        HttpRequest<?> request = HttpRequest.GET(uri);
        HttpResponse<?> response = client.exchange(request);
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION)).isEqualTo("/security/login");
        assertThat(userRepository.getEmails()).contains(email);

        uri = UriBuilder.of("/email").path("confirm").queryParam("token", "bogus").build();
        request = HttpRequest.GET(uri);
        response = client.exchange(request);
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION)).isEqualTo("/");
    }

    @Requires(property = "spec.name", value = "EmailConfirmationControllerTest")
    @Singleton
    static class UserRepositoryMock implements EmailConfirmationRepository {
        List<String> emails = new ArrayList<>();

        public List<String> getEmails() {
            return emails;
        }

        @Override
        public void enableByEmail(@NonNull @NotBlank String email) {
            emails.add(email);
        }
    }
}