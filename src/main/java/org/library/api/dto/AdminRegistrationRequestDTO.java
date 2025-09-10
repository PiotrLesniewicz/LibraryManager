package org.library.api.dto;

import org.library.domain.model.UserRole;

public record AdminRegistrationRequestDTO(
        String password,
        UserRole userRole,
        UserDTO userDTO,
        AddressDTO addressDTO,
        LibrarianDTO librarianDTO
) {
}
