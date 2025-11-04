package org.library.api.dto;

import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateUserDTO(
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
