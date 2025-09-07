package org.library.api.mapper;

import org.library.api.dto.AdminRegistrationRequestDTO;
import org.library.api.dto.RegistrationRequestDTO;
import org.library.api.dto.RegistrationResponseDTO;
import org.library.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UserDTOMapper.class, AddressDTOMapper.class, LibrarianDTOMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegistrationMapper {

    @Mapping(target = "userName", source = "userDTO.userName")
    @Mapping(target = "name", source = "userDTO.name")
    @Mapping(target = "surname", source = "userDTO.surname")
    @Mapping(target = "email", source = "userDTO.email")
    @Mapping(target = "phoneNumber", source = "userDTO.phoneNumber")
    @Mapping(target = "membershipDate", source = "userDTO.membershipDate")
    @Mapping(target = "address", source = "addressDTO")
    @Mapping(target = "librarian", source = "librarianDTO")
    User mapFromDto(AdminRegistrationRequestDTO registrationRequestDTO);

    @Mapping(target = "userName", source = "userDTO.userName")
    @Mapping(target = "name", source = "userDTO.name")
    @Mapping(target = "surname", source = "userDTO.surname")
    @Mapping(target = "email", source = "userDTO.email")
    @Mapping(target = "phoneNumber", source = "userDTO.phoneNumber")
    @Mapping(target = "membershipDate", source = "userDTO.membershipDate")
    @Mapping(target = "address", source = "addressDTO")
    User mapFromDto(RegistrationRequestDTO requestDTO);

    @Mapping(target = "userDTO.userName", source = "userName")
    @Mapping(target = "userDTO.name", source = "name")
    @Mapping(target = "userDTO.surname", source = "surname")
    @Mapping(target = "userDTO.email", source = "email")
    @Mapping(target = "userDTO.phoneNumber", source = "phoneNumber")
    @Mapping(target = "userDTO.membershipDate", source = "membershipDate")
    @Mapping(target = "addressDTO", source = "address")
    @Mapping(target = "librarianDTO", source = "librarian")
    RegistrationResponseDTO mapToDto(User user);
}
