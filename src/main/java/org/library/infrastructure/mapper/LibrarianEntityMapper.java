package org.library.infrastructure.mapper;

import org.library.domain.model.Librarian;
import org.library.domain.model.LibrarianRole;
import org.library.infrastructure.database.entity.LibrarianEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public interface LibrarianEntityMapper {

    @Mapping(target = "librarianRole", source = "librarianRole", qualifiedByName = "roleToString")
    @Mapping(target = "user", ignore = true)
    LibrarianEntity mapToEntity(Librarian librarian);

    @Mapping(target = "librarianRole", source = "librarianRole", qualifiedByName = "stringToRole")
    @Mapping(target = "user", ignore = true)
    Librarian mapFromEntity(LibrarianEntity librarianEntity);

    @Named("stringToRole")
    default LibrarianRole stringToRole(String role) {
        return role != null ? LibrarianRole.fromString(role) : null;
    }

    @Named("roleToString")
    default String roleToString(LibrarianRole userRole) {
        return userRole != null ? userRole.name() : null;
    }
}
