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

package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.AuthoritiesFetcher;
import org.projectcheckins.security.RegistrationCheckViolationException;
import org.projectcheckins.security.UserSave;

import java.util.Collections;

@MicronautTest(startApplication = false)
class EclipseStoreAuthoritiesFetcherTest {
    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";

    private final static String FOUND_EMAIL_WITH_ROLES = "pepito@unityfoundation.io";

    @Test
    void authoritiesFetcher(AuthoritiesFetcher authoritiesFetcher, EclipseStoreUser registerService) throws RegistrationCheckViolationException {
        assertThat(authoritiesFetcher.findAuthoritiesByEmail(NOT_FOUND_EMAIL))
            .isEmpty();
        registerService.register(new UserSave(FOUND_EMAIL, "password", Collections.emptyList()), null);
        assertThat(authoritiesFetcher.findAuthoritiesByEmail(FOUND_EMAIL))
            .isEmpty();
        registerService.register(new UserSave(FOUND_EMAIL_WITH_ROLES, "password", Collections.singletonList("ROLE_USER")), null);
        assertThat(authoritiesFetcher.findAuthoritiesByEmail(FOUND_EMAIL_WITH_ROLES))
            .containsExactly("ROLE_USER");

    }
}