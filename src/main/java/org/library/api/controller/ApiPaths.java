package org.library.api.controller;

public final class ApiPaths {

    private ApiPaths() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String USERS = "/users";
    public static final String PROFILE = "/profile";
    public static final String ADMIN = "/admin";

    // tests endpoint
    public static final String USER_PROFILE_ENDPOINT = USERS + PROFILE;
}
