package org.library.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum LibrarianRole {
    ADMIN("admin"),
    TECHNIC("technic"),
    ASSISTANT("assistant");

    private final String value;

    public static LibrarianRole fromString(String value) {
        return Arrays.stream(LibrarianRole.values())
                .filter(role -> role.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: [%s]".formatted(value)));
    }
}
