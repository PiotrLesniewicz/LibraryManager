package org.library.mapper;

import org.library.domain.model.Address;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.library.infrastructure.database.entity.AddressEntity;
import org.library.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public interface AddressEntityMapper {

    AddressEntity mapToEntity(Address address);

    @Mapping(target = "users", source = "users")
    Address mapFromEntity(AddressEntity addressEntity);

    default Set<User> mapUsers(Set<UserEntity> entities) {
        return entities.stream()
                .map(entity -> User.builder()
                        .userId(entity.getUserId())
                        .userName(entity.getUserName())
                        .name(entity.getName())
                        .surname(entity.getSurname())
                        .email(entity.getEmail())
                        .password(entity.getPassword())
                        .phoneNumber(entity.getPhoneNumber())
                        .membershipDate(entity.getMembershipDate())
                        .userRole(UserRole.fromString(entity.getUserRole()))
                        .build())
                .collect(Collectors.toSet());
    }

}
