package org.library.mapper;

import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.library.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {AddressEntityMapper.class}, unmappedSourcePolicy = ReportingPolicy.WARN)
public interface UserEntityMapper {

    @Mapping(target = "userRole", source = "userRole", qualifiedByName = "roleToString")
    @Mapping(target = "opinions", ignore = true)
    @Mapping(target = "librarian", ignore = true)
    UserEntity mapToEntity(User user);

    @Mapping(target = "userRole", source = "userRole", qualifiedByName = "stringToRole")
    @Mapping(target = "librarian", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "loans", ignore = true)
    @Mapping(target = "opinions", ignore = true)
    User mapFromEntity(UserEntity userEntity);

    @Named("stringToRole")
    default UserRole stringToRole(String role) {
        return role != null ? UserRole.fromString(role) : null;
    }

    @Named("roleToString")
    default String roleToString(UserRole userRole) {
        return userRole != null ? userRole.name() : null;
    }
}
