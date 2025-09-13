package org.library.infrastructure.mapper;

import org.library.domain.model.User;
import org.library.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {AddressEntityMapper.class, LibrarianEntityMapper.class}, unmappedSourcePolicy = ReportingPolicy.WARN)
public interface UserEntityMapper {

    @Mapping(target = "opinions", ignore = true)
    UserEntity mapToEntity(User user);

    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "loans", ignore = true)
    @Mapping(target = "opinions", ignore = true)
    User mapFromEntity(UserEntity userEntity);
}
