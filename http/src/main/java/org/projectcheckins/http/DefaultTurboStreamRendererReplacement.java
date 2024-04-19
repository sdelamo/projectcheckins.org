package org.projectcheckins.http;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.ViewsModelDecorator;
import io.micronaut.views.ViewsRendererLocator;
import io.micronaut.views.turbo.DefaultTurboStreamRenderer;
import io.micronaut.views.turbo.TurboStream;
import io.micronaut.views.turbo.TurboStreamRenderer;
import io.micronaut.views.turbo.http.TurboMediaType;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
@Replaces(TurboStreamRenderer.class)

public class DefaultTurboStreamRendererReplacement implements TurboStreamRenderer {
    protected final ViewsRendererLocator viewsRendererLocator;
    private final ViewsModelDecorator viewsModelDecorator;

    public DefaultTurboStreamRendererReplacement(ViewsRendererLocator viewsRendererLocator,
                                                 ViewsModelDecorator viewsModelDecorator) {
        this.viewsRendererLocator = viewsRendererLocator;
        this.viewsModelDecorator = viewsModelDecorator;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public Optional<Writable> render(@NonNull TurboStream.Builder builder,
                                     @Nullable HttpRequest<?> request) {
        return builder.getTemplateView()
                .map(viewName ->  {
                    Object model =  builder.getTemplateModel().orElse(null);
                    ModelAndView<Object> modelAndView = new ModelAndView<>(viewName, model);
                    if (request != null) {
                        viewsModelDecorator.decorate(request, modelAndView);
                    }
                    Object decoratedModel = modelAndView.getModel().orElse(null);
                    return viewsRendererLocator.resolveViewsRenderer(viewName, TurboMediaType.TURBO_STREAM, decoratedModel)
                            .flatMap(renderer -> builder.template(renderer.render(viewName, decoratedModel, request))
                                    .build()
                                    .render());
                })
                .orElseGet(() -> builder.build().render());
    }
}
