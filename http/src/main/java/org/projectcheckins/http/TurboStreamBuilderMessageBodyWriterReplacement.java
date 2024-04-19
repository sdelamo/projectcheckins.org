package org.projectcheckins.http;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.core.type.MutableHeaders;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.codec.CodecException;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.views.exceptions.ViewRenderingException;
import io.micronaut.views.turbo.TurboStream;
import io.micronaut.views.turbo.TurboStreamBuilderMessageBodyWriter;
import io.micronaut.views.turbo.TurboStreamRenderer;
import io.micronaut.views.turbo.http.TurboMediaType;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

@Produces(TurboMediaType.TURBO_STREAM)
@Singleton
@Replaces(TurboStreamBuilderMessageBodyWriter.class)
public class TurboStreamBuilderMessageBodyWriterReplacement extends TurboStreamBuilderMessageBodyWriter {
    private static final Logger LOG = LoggerFactory.getLogger(TurboStreamBuilderMessageBodyWriter.class);

    private final TurboStreamRenderer turboStreamRenderer;

    public TurboStreamBuilderMessageBodyWriterReplacement(TurboStreamRenderer turboStreamRenderer) {
        super(turboStreamRenderer);
        this.turboStreamRenderer = turboStreamRenderer;
    }

    @Override
    public void writeTo(@NonNull Argument<TurboStream.Builder> type,
                        @NonNull MediaType mediaType,
                        TurboStream.Builder turboStreamBuilder,
                        @NonNull MutableHeaders outgoingHeaders,
                        @NonNull OutputStream outputStream) throws CodecException {
        outgoingHeaders.set(HttpHeaders.CONTENT_TYPE, TurboMediaType.TURBO_STREAM);
        turboStreamRenderer.render(turboStreamBuilder, ServerRequestContext.currentRequest().orElse(null))
                .ifPresent(writable -> {
                    try {
                        writable.writeTo(outputStream);
                    } catch (IOException e) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error("IOException writing TurboStream Writeable to OutputStream", e);
                        }
                        throw new ViewRenderingException("IOException writing TurboStream Writeable to OutputStream", e);
                    }
                });

    }
}
