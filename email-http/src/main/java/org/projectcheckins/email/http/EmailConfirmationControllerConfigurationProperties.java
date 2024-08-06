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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

@ConfigurationProperties(EmailConfirmationControllerConfigurationProperties.PREFIX)
public class EmailConfirmationControllerConfigurationProperties implements EmailConfirmationControllerConfiguration {
    public static final String PREFIX = "email.confirmation";
    public static final String DEFAULT_PATH = "/email/confirm";
    public static final String DEFAULT_SUCCESSFUL_REDIRECT = "/security/login";
    public static final String DEFAULT_FAILURE_REDIRECT = "/";
    private String path = DEFAULT_PATH;
    private String successfulRedirect = DEFAULT_SUCCESSFUL_REDIRECT;
    private String failureRedirect = DEFAULT_FAILURE_REDIRECT;

    @Override
    @NonNull
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    @NonNull
    public String getSuccessfulRedirect() {
        return successfulRedirect;
    }

    @Override
    public String getFailureRedirect() {
        return failureRedirect;
    }

    public void setFailureRedirect(String failureRedirect) {
        this.failureRedirect = failureRedirect;
    }

    public void setSuccessfulRedirect(String successfulRedirect) {
        this.successfulRedirect = successfulRedirect;
    }
}
