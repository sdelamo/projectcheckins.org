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

import io.micronaut.core.annotation.NonNull;

public interface EmailConfirmationControllerConfiguration {

    /**
     * @return The path to send the user to to confirm its email. Typically, a link in an email.
     */
    @NonNull
    String getPath();

    /**
     * @return The path to redirect to after a successfully confirming a user's email
     */
    @NonNull
    String getSuccessfulRedirect();

    /**
     * @return The path to redirect to after an unsuccessful confirmation.
     */
    @NonNull
    String getFailureRedirect();
}
