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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.AuthoritiesFetcher;

import java.util.Collections;
import java.util.List;

@Singleton
class EclipseStoreAuthoritiesFetcher implements AuthoritiesFetcher {
    private final RootProvider<Data> rootProvider;

    EclipseStoreAuthoritiesFetcher(RootProvider<Data> rootProvider) {
        this.rootProvider = rootProvider;
    }

    @Override
    @NonNull
    public List<String> findAuthoritiesByEmail(@NotBlank String email) {
        return rootProvider.root().getUsers()
                .stream()
                .filter(user -> user.email().equals(email))
                .map(UserEntity::authorities)
                .findFirst()
                .orElseGet(Collections::emptyList);
    }
}
