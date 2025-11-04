package org.library.api.mapper;

import org.library.api.dto.AdminRegistrationRequestDTO;
import org.library.api.dto.RegistrationRequestDTO;
import org.library.api.dto.UpdateUserDTO;
import org.library.api.dto.UserDTO;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.User;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;


@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDTOMapper {

    UserDTO mapToDto(User user);

    UserDTO.AddressDTO mapToAddressDto(Address address);

    UserDTO.LibrarianDTO mapToLibrarianDto(Librarian librarian);

    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "unwrap")
    @Mapping(source = "city", target = "address.city")
    @Mapping(source = "street", target = "address.street")
    @Mapping(source = "number", target = "address.number")
    @Mapping(source = "postCode", target = "address.postCode")
    User mapFromDto(UpdateUserDTO userDTO);

    @Mapping(source = "city", target = "address.city")
    @Mapping(source = "street", target = "address.street")
    @Mapping(source = "number", target = "address.number")
    @Mapping(source = "postCode", target = "address.postCode")
    User mapFromDto(RegistrationRequestDTO registrationRequestDTO);

    User mapFromDto(AdminRegistrationRequestDTO registrationRequestDTO);

    /**
     * Converts JsonNullable to a value that can be processed by domain layer.
     * Returns empty string as a sentinel value when field should be cleared (nullable contains null).
     * This allows distinguishing between "field not provided" (null) and "clear field" (empty string).
     * Empty string is safe to use as a marker because validation ensures it's not a valid phone number.
     *
     * @param nullable JsonNullable wrapper from PATCH request DTO
     * @return null (no update), "" (clear field), or actual value (update field)
     */
    @Named("unwrap")
    default String unwrap(JsonNullable<String> nullable) {
        final String emptyMarker = "";
        if (nullable == null || !nullable.isPresent()) {
            return null;
        }
        return nullable.get() == null ? emptyMarker : nullable.get();
    }
}
