package org.projectcheckins.netty;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;

import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.annotations.Generated;

@Generated// "ignore for jacoco"
@Requires(env = Environment.DEVELOPMENT)
@Singleton
public class Bootstrap implements ApplicationEventListener<ServerStartupEvent> {

    private final RegisterService registerService;

    public Bootstrap(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        try {
            registerService.register("delamos@unityfoundation.io", "secret");
            registerService.register("calvog@unityfoundation.io", "secret");
        } catch (UserAlreadyExistsException e) {
        }
    }
}
