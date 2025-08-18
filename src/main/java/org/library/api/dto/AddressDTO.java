package org.library.api.dto;

public record AddressDTO(
        String city,
        String street,
        String number,
        String postCode
) {

}
