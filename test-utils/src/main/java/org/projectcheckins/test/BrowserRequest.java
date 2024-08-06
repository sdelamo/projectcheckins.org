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

import io.micronaut.http.HttpRequestFactory;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpRequest;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

public final class BrowserRequest {
    private BrowserRequest() {
    }

    public static <T> MutableHttpRequest<T> POST(URI uri, T body) {
        return POST(uri.toString(), body);
    }

    public static <T> MutableHttpRequest<T> POST(String uri, T body) {
        Objects.requireNonNull(uri, "Argument [uri] is required");
        return HttpRequestFactory.INSTANCE.post(uri, body)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.TEXT_HTML);
    }

    public static <T> MutableHttpRequest<T> POST(String uri, Map<CharSequence, CharSequence> headers, T body) {
        return POST(uri, body).headers(headers);
    }

    public static MutableHttpRequest<?> GET(String uri) {
        return HttpRequestFactory.INSTANCE.get(uri).accept(MediaType.TEXT_HTML);
    }

    public static MutableHttpRequest<?> GET(String uri, Map<CharSequence, CharSequence> headers) {
        return GET(uri).headers(headers);
    }

    public static MutableHttpRequest<?> GET(URI uri) {
        return GET(uri.toString());
    }

}
