package org.library.api.dto;

import org.library.domain.model.UserRole;

public record LibrarianRegistrationRequestDTO(
        String password,
        UserRole userRole,
        UserDTO userDTO,
        AddressDTO addressDTO,
        LibrarianDTO librarianDTO
) {
}
