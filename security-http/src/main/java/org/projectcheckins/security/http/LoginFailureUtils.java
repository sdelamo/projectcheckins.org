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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationResponse;

import java.net.URI;
import java.util.Optional;

public final class LoginFailureUtils {
    private LoginFailureUtils() {
    }

    @NonNull
    public static Optional<URI> loginFailure(@Nullable String loginFailure,
                               @NonNull AuthenticationResponse authenticationFailed) {
        if (loginFailure == null) {
            return Optional.empty();
        }
        UriBuilder uriBuilder = UriBuilder.of(loginFailure);
        if (authenticationFailed instanceof AuthenticationFailed failure) {
            uriBuilder = uriBuilder.queryParam("reason", failure.getReason());
        }
        return Optional.of(uriBuilder.build());

    }
}
