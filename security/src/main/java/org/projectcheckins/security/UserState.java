package org.projectcheckins.security;

public interface UserState {

    String getId();

    boolean isEnabled();

    String getEmail();

    String getPassword();
}
