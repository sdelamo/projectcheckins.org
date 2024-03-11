package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.model.ViewModelProcessor;

import java.util.HashMap;
import java.util.Map;

abstract class MapViewModelProcessor implements ViewModelProcessor<Map<String, Object>> {
    @Override
    public void process(@NonNull HttpRequest<?> request, @NonNull ModelAndView<Map<String, Object>> modelAndView) {
        Map<String, Object> viewModel = modelAndView.getModel().orElseGet(() -> {
            final HashMap<String, Object> newModel = new HashMap<>(1);
            modelAndView.setModel(newModel);
            return newModel;
        });
        try {
            populateModel(viewModel);
        } catch (UnsupportedOperationException ex) {
            final HashMap<String, Object> modifiableModel = new HashMap<>(viewModel);
            populateModel(modifiableModel);
            modelAndView.setModel(modifiableModel);
        }
    }

    protected abstract void populateModel(Map<String, Object> viewModel);
}
