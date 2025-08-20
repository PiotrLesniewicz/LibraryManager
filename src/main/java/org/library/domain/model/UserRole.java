package org.library.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("user"),
    LIBRARIAN("librarian");

    private static final String ROLE = "ROLE_";

    private final String value;

    public String asAuthority() {
        return ROLE + name();
    }

    public static UserRole fromString(String value) {
        return Arrays.stream(UserRole.values())
                .filter(role -> role.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: [%s]".formatted(value)));
    }
}


