package org.projectcheckins.security.http;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.SecurityFilter;
import jakarta.inject.Singleton;
import org.projectcheckins.security.MapViewModelProcessor;
import java.util.Map;
import java.util.Optional;

@Singleton
public class UserViewModelProcessor extends MapViewModelProcessor {
    private static final String KEY_SECURITY = "user";

    @Override
    protected void populateModel(HttpRequest<?> request, Map<String, Object> viewModel) {
        getAuthentication(request)
                .ifPresent(authentication -> viewModel.put(KEY_SECURITY, authentication));
    }

    protected Optional<Authentication> getAuthentication(HttpRequest<?> request) {
        return request.getAttribute(SecurityFilter.AUTHENTICATION, Authentication.class);
    }
}
