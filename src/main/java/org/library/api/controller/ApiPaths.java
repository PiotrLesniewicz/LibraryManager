package org.library.api.controller;

public final class ApiPaths {

    private ApiPaths() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String IDENTIFIER_PLACEHOLDER = "/{identifier}";
    public static final String ADMIN = "/admin";
    public static final String USERS = "/users";
    public static final String PROFILE = "/profile";
    public static final String RELATIVE_USER_BY_IDENTIFIER = USERS + IDENTIFIER_PLACEHOLDER;
    public static final String REMOVE_LIBRARIAN_ROLE = RELATIVE_USER_BY_IDENTIFIER + "/librarian";

    // tests endpoint User
    public static final String USER_PROFILE_ENDPOINT = USERS + PROFILE;

    // test endpoint Admin
    public static final String ADMIN_USERS = ADMIN + USERS;
}
