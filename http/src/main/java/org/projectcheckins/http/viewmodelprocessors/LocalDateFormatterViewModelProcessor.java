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

package org.projectcheckins.http.viewmodelprocessors;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.locale.HttpLocaleResolver;
import jakarta.inject.Singleton;
import org.projectcheckins.security.MapViewModelProcessor;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class LocalDateFormatterViewModelProcessor extends MapViewModelProcessor {
    private static final String MODEL_DATE_TIME_FORMATTER= "dateTimeFormatter";
    private final HttpLocaleResolver httpLocaleResolver;
    private final MessageSource messageSource;
    private final Map<Locale, CustomDateTimeFormatter> formatters = new ConcurrentHashMap<>();

    public LocalDateFormatterViewModelProcessor(HttpLocaleResolver httpLocaleResolver,
                                                MessageSource messageSource) {
        this.httpLocaleResolver = httpLocaleResolver;
        this.messageSource = messageSource;
    }

    @Override
    protected void populateModel(@NonNull HttpRequest<?> request, @NonNull Map<String, Object> viewModel) {
        Locale locale = httpLocaleResolver.resolveOrDefault(request);
        viewModel.put(MODEL_DATE_TIME_FORMATTER,
                formatters.computeIfAbsent(locale, l -> new LocalizedCustomDateTimeFormatter(messageSource, l)));
    }
}
