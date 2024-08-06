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

package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.config.RedirectConfiguration;
import io.micronaut.security.config.RedirectService;
import io.micronaut.security.config.SecurityConfigurationProperties;
import io.micronaut.security.errors.PriorToLoginPersistence;
import io.micronaut.security.token.cookie.AccessTokenCookieConfiguration;
import io.micronaut.security.token.cookie.RefreshTokenCookieConfiguration;
import io.micronaut.security.token.cookie.TokenCookieLoginHandler;
import io.micronaut.security.token.generator.AccessRefreshTokenGenerator;
import io.micronaut.security.token.generator.AccessTokenConfiguration;
import jakarta.inject.Singleton;

@Requires(property = SecurityConfigurationProperties.PREFIX + ".authentication", value = "cookie")
@Requires(classes = TokenCookieLoginHandler.class)
@Singleton
@Replaces(TokenCookieLoginHandler.class)
public class TokenCookieLoginHandlerReplacement extends TokenCookieLoginHandler {
    public TokenCookieLoginHandlerReplacement(RedirectService redirectService,
                                              RedirectConfiguration redirectConfiguration,
                                              AccessTokenCookieConfiguration accessTokenCookieConfiguration,
                                              RefreshTokenCookieConfiguration refreshTokenCookieConfiguration,
                                              AccessTokenConfiguration accessTokenConfiguration,
                                              AccessRefreshTokenGenerator accessRefreshTokenGenerator,
                                              @Nullable PriorToLoginPersistence<HttpRequest<?>, MutableHttpResponse<?>> priorToLoginPersistence) {
        super(redirectService, redirectConfiguration, accessTokenCookieConfiguration, refreshTokenCookieConfiguration, accessTokenConfiguration, accessRefreshTokenGenerator, priorToLoginPersistence);
    }

    @Override
    public MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationFailed, HttpRequest<?> request) {
        return LoginFailureUtils.loginFailure(loginFailure, authenticationFailed)
                .map(HttpResponse::seeOther)
                .orElseGet(HttpResponse::unauthorized);
    }
}
