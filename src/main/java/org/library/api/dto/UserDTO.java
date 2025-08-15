package org.library.api.dto;

import java.time.LocalDate;

public record UserDTO(
        String userName,
        String name,
        String surname,
        String email,
        String phoneNumber,
        LocalDate membershipDate
) {
}
