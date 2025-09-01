package org.library.api.dto;

public record RegistrationRequestDTO(
        String password,
        UserDTO userDTO,
        AddressDTO addressDTO
) {
}
