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

package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.model.ViewModelProcessor;

import java.util.HashMap;
import java.util.Map;

public abstract class MapViewModelProcessor implements ViewModelProcessor<Map<String, Object>> {
    @Override
    public void process(@NonNull HttpRequest<?> request, @NonNull ModelAndView<Map<String, Object>> modelAndView) {
        Map<String, Object> viewModel = modelAndView.getModel().orElseGet(() -> {
            final HashMap<String, Object> newModel = new HashMap<>(1);
            modelAndView.setModel(newModel);
            return newModel;
        });
        try {
            populateModel(request, viewModel);
        } catch (UnsupportedOperationException ex) {
            final HashMap<String, Object> modifiableModel = new HashMap<>(viewModel);
            populateModel(request, modifiableModel);
            modelAndView.setModel(modifiableModel);
        }
    }

    protected abstract void populateModel(@NonNull HttpRequest<?> request, @NonNull Map<String, Object> viewModel);
}
