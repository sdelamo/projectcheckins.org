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

package org.projectcheckins.test;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import org.reactivestreams.Publisher;

import java.util.Collections;

public abstract class AbstractAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {

    public static final Authentication ADMIN = Authentication.build("admin", Collections.singletonList("ROLE_ADMIN"), Collections.singletonMap("email", "admin@unityfoundation.io"));
    public static final Authentication SDELAMO = Authentication.build("xxx", Collections.emptyList(), Collections.singletonMap("email", "delamos@unityfoundation.io"));
    private Authentication authentication;

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        return Publishers.just(authentication);
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
