package org.library.mapper;

import org.library.domain.model.Librarian;
import org.library.domain.model.Role;
import org.library.infrastructure.database.entity.LibrarianEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public interface LibrarianEntityMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    LibrarianEntity mapToEntity(Librarian librarian);


    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    Librarian mapFromEntity(LibrarianEntity librarianEntity);

    @Named("stringToRole")
    default Role stringToRole(String role) {
        return role != null ? Role.fromString(role) : null;
    }

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }
}
