package org.library.api.dto;

public record RegistrationResponseDTO(
        UserDTO userDTO,
        AddressDTO addressDTO,
        LibrarianDTO librarianDTO
) {
}
