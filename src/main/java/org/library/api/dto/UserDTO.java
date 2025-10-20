package org.library.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.library.domain.model.LibrarianRole;

import java.time.LocalDate;

public record UserDTO(
        String userName,
        String name,
        String surname,
        String email,
        @JsonInclude(JsonInclude.Include.ALWAYS)
        String phoneNumber,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDate membershipDate,
        AddressDTO address,
        LibrarianDTO librarian) {

    public record AddressDTO(
            String city,
            String street,
            String number,
            String postCode
    ) {

    }

    public record LibrarianDTO(
            LibrarianRole librarianRole,
            @JsonProperty(access = JsonProperty.Access.READ_ONLY)
            LocalDate hireDate
    ) {
    }
}


