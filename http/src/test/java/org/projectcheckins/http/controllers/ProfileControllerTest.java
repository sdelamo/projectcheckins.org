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

package org.projectcheckins.http.controllers;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AssertUtils.htmlBody;
import static org.projectcheckins.test.AssertUtils.htmlPage;
import static org.projectcheckins.test.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileRecord;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.test.BrowserRequest;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "ProfileControllerTest")
@MicronautTest
class ProfileControllerTest {

  @Test
  @SuppressWarnings("rawtypes")
  void crud(@Client("/") HttpClient httpClient) {
    BlockingHttpClient client = httpClient.toBlocking();
    Map<CharSequence, CharSequence> auth = Map.of(AUTHORIZATION, "Basic YWRtaW46YWRtaW4=");

    assertThat(client.exchange(BrowserRequest.GET("/profile/show", auth), String.class))
        .satisfies(htmlPage())
        .satisfies(htmlBody("/security/changePassword"))
        .satisfies(htmlBody("/profile/edit"));

    assertThat(client.exchange(BrowserRequest.GET("/profile/edit", auth), String.class))
        .satisfies(htmlPage())
        .satisfies(htmlBody("/profile/update"));

    assertThat(client.exchange(BrowserRequest.POST("/profile/update", auth, Map.of(
        "timeZone", TimeZone.getDefault().getID(),
        "firstDayOfWeek", DayOfWeek.MONDAY.name(),
        "beginningOfDay", "09:00",
        "endOfDay", "16:30",
        "timeFormat", TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
            "format", Format.WYSIWYG,
        "firstName", "Guillermo",
        "lastName", "Calvo"))))
        .satisfies(redirection("/profile/show"));

      assertThat(client.exchange(BrowserRequest.GET("/profile/show", auth), String.class))
              .satisfies(htmlPage())
              .satisfies(htmlBody("Guillermo Calvo"))
              .satisfies(htmlBody("Rich Text"));
  }

  @Requires(property = "spec.name", value = "ProfileControllerTest")
  @Singleton
  static class AuthenticationProviderMock<B> implements HttpRequestAuthenticationProvider<B> {
    @Override
    public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {
      return AuthenticationResponse.success(authRequest.getIdentity(), Map.of("email", "email@example.com"));
    }
  }

  @Requires(property = "spec.name", value = "ProfileControllerTest")
  @Singleton
  @Replaces(ProfileRepository.class)
  static class ProfileRepositoryMock implements ProfileRepository {

    @Override
    public List<ProfileRecord> list(Tenant tenant) {
      return List.of(new ProfileRecord(
              "id",
              "calvog@unityfoundation.io",
              TimeZone.getDefault(),
              DayOfWeek.MONDAY,
              LocalTime.of(9, 0),
              LocalTime.of(16, 30),
              TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
              Format.WYSIWYG,
              "Guillermo",
              "Calvo",
              true
      ));
    }

    @Override
    public Optional<ProfileRecord> findById(String id, Tenant tenant) {
      return Optional.of(new ProfileRecord(
              id,
              "calvog@unityfoundation.io",
              TimeZone.getDefault(),
              DayOfWeek.MONDAY,
              LocalTime.of(9, 0),
              LocalTime.of(16, 30),
              TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
              Format.WYSIWYG,
              "Guillermo",
              "Calvo",
              true
      ));
    }

    @Override
    public void update(Authentication authentication, ProfileUpdate profileUpdate, Tenant tenant) {

    }
  }
}
