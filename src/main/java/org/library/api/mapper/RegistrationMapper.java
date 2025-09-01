package org.library.api.mapper;

import org.library.api.dto.*;
import org.library.domain.model.Address;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UserDTOMapper.class, AddressDTOMapper.class, LibrarianDTOMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegistrationMapper {

    default User mapFromDto(RegistrationRequestDTO requestDTO) {
        return User.builder()
                .userName(requestDTO.userDTO().userName())
                .name(requestDTO.userDTO().name())
                .surname(requestDTO.userDTO().surname())
                .email(requestDTO.userDTO().email())
                .password(requestDTO.password())
                .phoneNumber(requestDTO.userDTO().phoneNumber())
                .address(
                        Address.builder()
                                .city(requestDTO.addressDTO().city())
                                .street(requestDTO.addressDTO().street())
                                .number(requestDTO.addressDTO().number())
                                .postCode(requestDTO.addressDTO().postCode())
                                .build()
                )
                .build();
    }

    default RegistrationResponseDTO mapToDto(User user) {
        return new RegistrationResponseDTO(
                getUserDTO(user),
                getAddressDTO(user),
                getLibrarianDTO(user)
        );
    }

    private static UserDTO getUserDTO(User user) {
        return new UserDTO(
                user.getUserName(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getMembershipDate()
        );
    }

    private static AddressDTO getAddressDTO(User user) {
        return new AddressDTO(
                user.getAddress().getCity(),
                user.getAddress().getStreet(),
                user.getAddress().getNumber(),
                user.getAddress().getPostCode()
        );
    }

    private static LibrarianDTO getLibrarianDTO(User user) {
        return user.getUserRole() != UserRole.LIBRARIAN
                ? null
                : new LibrarianDTO(
                user.getLibrarian().getLibrarianRole(),
                user.getLibrarian().getHireDate()
        );
    }
}
