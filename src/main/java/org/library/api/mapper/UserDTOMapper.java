package org.library.api.mapper;

import org.library.api.dto.UserDTO;
import org.library.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDTOMapper {

    UserDTO mapToDTO(User user);
}
