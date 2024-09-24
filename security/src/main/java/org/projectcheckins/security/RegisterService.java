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

package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

public interface RegisterService {

    @NonNull
    default String register(@NonNull @NotBlank String email,
                            @NonNull @NotBlank String rawPassword,
                            @Nullable Tenant tenant) throws RegistrationCheckViolationException {
        return register(email, rawPassword, tenant, Collections.emptyList());
    }

    @NonNull String register(@NonNull @NotBlank String email,
                    @NonNull @NotBlank String rawPassword,
                    @Nullable Tenant tenant,
                    @NonNull List<String> authorities) throws RegistrationCheckViolationException;
}
