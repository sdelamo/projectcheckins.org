package org.projectcheckins.http.viewmodelprocessors;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.locale.HttpLocaleResolver;
import io.micronaut.views.ModelAndView;
import jakarta.inject.Singleton;
import org.projectcheckins.core.viewmodelprocessors.MapViewModelProcessor;

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
    public void process(@NonNull HttpRequest<?> request, @NonNull ModelAndView<Map<String, Object>> modelAndView) {
        Locale locale = httpLocaleResolver.resolveOrDefault(request);
        populateModelAndView(modelAndView, viewModel ->
                viewModel.put(MODEL_DATE_TIME_FORMATTER,
                        formatters.computeIfAbsent(locale, l -> new LocalizedCustomDateTimeFormatter(messageSource, l))));
    }
}
