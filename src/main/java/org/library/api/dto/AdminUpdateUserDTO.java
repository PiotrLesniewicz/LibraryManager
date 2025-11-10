package org.library.api.dto;

import org.library.domain.model.LibrarianRole;
import org.library.domain.model.UserRole;
import org.openapitools.jackson.nullable.JsonNullable;

public record AdminUpdateUserDTO(
        UserRole userRole,
        LibrarianRole librarianRole,
        String userName,
        String name,
        String surname,
        String email,
        JsonNullable<String> phoneNumber,
        String city,
        String street,
        String number,
        String postCode
) {
}
