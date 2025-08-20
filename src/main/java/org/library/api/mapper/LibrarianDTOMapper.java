package org.library.api.mapper;

import org.library.api.dto.LibrarianDTO;
import org.library.domain.model.Librarian;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibrarianDTOMapper {

    LibrarianDTO mapToDto(Librarian librarian);
}
