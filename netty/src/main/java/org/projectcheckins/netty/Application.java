package org.projectcheckins.netty;

import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.Generated;
import io.micronaut.runtime.Micronaut;

@Generated// "ignore for jacoco"
public class Application {
    public static void main(String[] args) {
        Micronaut.build(args)
                .defaultEnvironments(Environment.DEVELOPMENT)
                .start();
    }
}