package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.ViewsModelDecorator;
import io.micronaut.views.ViewsRendererLocator;
import io.micronaut.views.turbo.TurboFrame;
import io.micronaut.views.turbo.TurboFrameRenderer;
import jakarta.inject.Singleton;

import java.util.Optional;

@Replaces(TurboFrameRenderer.class)
@Singleton
class TurboFrameRendererReplacement implements TurboFrameRenderer {
    private final ViewsModelDecorator viewsModelDecorator;
    private final ViewsRendererLocator viewsRendererLocator;
    private final String mediaType;

    TurboFrameRendererReplacement(ViewsModelDecorator viewsModelDecorator,
                                            ViewsRendererLocator viewsRendererLocator) {
        this.viewsModelDecorator = viewsModelDecorator;
        this.viewsRendererLocator = viewsRendererLocator;
        this.mediaType = MediaType.TEXT_HTML;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Optional<Writable> render(TurboFrame.Builder builder, @Nullable HttpRequest<?> request) {
        return builder.getTemplateView()
                .map(viewName -> {
                    Object model = builder.getTemplateModel().orElse(null);
                    ModelAndView<Object> modelAndView = new ModelAndView<>(viewName, model);
                    if (request != null && viewsModelDecorator != null) {
                        viewsModelDecorator.decorate(request, modelAndView);
                    }
                    Object decoratedModel = modelAndView.getModel().orElse(null);
                    return viewsRendererLocator.resolveViewsRenderer(viewName, mediaType, decoratedModel)
                            .flatMap(renderer -> builder.template(renderer.render(viewName, decoratedModel, request))
                                    .build()
                                    .render());
                })
                .orElseGet(() -> builder.build().render());
    }

}
