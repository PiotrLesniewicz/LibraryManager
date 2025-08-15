package org.library.api.mapper;

import org.library.api.dto.UserDTO;
import org.library.api.security.LibraryUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDTOMapper {

    UserDTO mapToDTO(LibraryUserDetails userDetails);
}
