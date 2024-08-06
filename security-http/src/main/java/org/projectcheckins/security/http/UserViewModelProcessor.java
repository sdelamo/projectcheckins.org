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

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.SecurityFilter;
import jakarta.inject.Singleton;
import org.projectcheckins.security.MapViewModelProcessor;
import java.util.Map;
import java.util.Optional;

@Singleton
public class UserViewModelProcessor extends MapViewModelProcessor {
    private static final String KEY_SECURITY = "user";

    @Override
    protected void populateModel(HttpRequest<?> request, Map<String, Object> viewModel) {
        getAuthentication(request)
                .ifPresent(authentication -> viewModel.put(KEY_SECURITY, authentication));
    }

    protected Optional<Authentication> getAuthentication(HttpRequest<?> request) {
        return request.getAttribute(SecurityFilter.AUTHENTICATION, Authentication.class);
    }
}
