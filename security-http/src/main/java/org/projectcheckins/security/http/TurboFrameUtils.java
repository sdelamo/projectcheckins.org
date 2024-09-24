package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.views.TemplatedBuilder;
import io.micronaut.views.turbo.TurboFrame;
import io.micronaut.views.turbo.http.TurboHttpHeaders;

import java.util.Map;
import java.util.Optional;

public final class TurboFrameUtils {
    private TurboFrameUtils() {

    }

    public static Optional<String> turboFrame(@NonNull HttpRequest<?> request) {
        return request.getHeaders().get(TurboHttpHeaders.TURBO_FRAME, String.class);
    }

    public static Optional<Object> turboFrame(@NonNull HttpRequest<?> request,
                                                                    @NonNull String viewName,
                                                                    @NonNull Map<String, Object> model) {
        return turboFrame(request).map(id -> turboFrame(id, viewName, model));
    }

    public static TemplatedBuilder<TurboFrame> turboFrame(@NonNull String id,
                                                                    @NonNull String viewName,
                                                                    @NonNull Map<String, Object> model) {
        return TurboFrame.builder().id(id).template(viewName, model);
    }
}
