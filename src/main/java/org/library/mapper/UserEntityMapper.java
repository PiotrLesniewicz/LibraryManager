package org.library.mapper;

import org.library.domain.model.Role;
import org.library.domain.model.User;
import org.library.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {AddressEntityMapper.class}, unmappedSourcePolicy = ReportingPolicy.WARN)
public interface UserEntityMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    @Mapping(target = "librarians")
    UserEntity mapToEntity(User user);

    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    @Mapping(target = "librarians", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "loans", ignore = true)
    User mapFromEntity(UserEntity userEntity);

    @Named("stringToRole")
    default Role stringToRole(String role) {
        return role != null ? Role.fromString(role) : null;
    }

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }
}
