package org.library.infrastructure.mapper;

import org.library.domain.model.Librarian;
import org.library.infrastructure.database.entity.LibrarianEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public interface LibrarianEntityMapper {

    @Mapping(target = "user", ignore = true)
    LibrarianEntity mapToEntity(Librarian librarian);

    @Mapping(target = "user", ignore = true)
    Librarian mapFromEntity(LibrarianEntity librarianEntity);
}
