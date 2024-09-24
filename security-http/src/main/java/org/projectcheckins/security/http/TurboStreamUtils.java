package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.views.turbo.TurboStream;
import io.micronaut.views.turbo.http.TurboHttpHeaders;
import io.micronaut.views.turbo.http.TurboMediaType;

import java.util.Map;
import java.util.Optional;

public final class TurboStreamUtils {
    private TurboStreamUtils() {

    }

    @NonNull
    public static Optional<TurboStream.Builder> turboStream(@NonNull HttpRequest<?> request,
                                                            @NonNull String viewName,
                                                            @NonNull Map<String, Object> model) {
        if (!TurboMediaType.acceptsTurboStream(request)) {
            return Optional.empty();
        }
        String turboFrame = request.getHeaders().get(TurboHttpHeaders.TURBO_FRAME);
        if (turboFrame == null) {
            return Optional.empty();
        }
        return Optional.of(TurboStream.builder()
                .targetDomId(turboFrame)
                .template(viewName, model)
                .update());
    }
}
