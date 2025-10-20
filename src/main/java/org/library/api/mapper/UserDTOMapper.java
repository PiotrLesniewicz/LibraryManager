package org.library.api.mapper;

import org.library.api.dto.UserDTO;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDTOMapper {

    UserDTO mapToDto(User user);

    UserDTO.AddressDTO mapToAddressDto(Address address);

    UserDTO.LibrarianDTO mapToLibrarianDto(Librarian librarian);
}
