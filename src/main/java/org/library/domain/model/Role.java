package org.library.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("user"),
    LIBRARIAN("librarian"),
    ADMIN("admin"),
    TECHNIC("technic");

    private final String value;

    public static Role fromString(String value) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: [%s]".formatted(value)));
    }
}


