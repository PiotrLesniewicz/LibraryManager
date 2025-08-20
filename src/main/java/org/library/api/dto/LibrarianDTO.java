package org.library.api.dto;

import org.library.domain.model.LibrarianRole;

import java.time.LocalDate;

public record LibrarianDTO(
        LibrarianRole librarianRole,
        LocalDate hireDate
) {
}
