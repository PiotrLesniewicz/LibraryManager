package org.library.api.mapper;

import org.library.api.dto.AddressDTO;
import org.library.domain.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressDTOMapper {
    AddressDTO mapToDto(Address address);
}
