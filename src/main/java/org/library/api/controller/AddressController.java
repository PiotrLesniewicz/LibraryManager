package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.AddressDTO;
import org.library.api.mapper.AddressDTOMapper;
import org.library.api.security.LibraryUserDetails;
import org.library.domain.model.Address;
import org.library.domain.service.AddressService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.USER)
@AllArgsConstructor
public class AddressController {

    private AddressService addressService;
    private AddressDTOMapper mapping;

    static final String ADDRESS_INFO = "me/address";

    @GetMapping(ADDRESS_INFO)
    public AddressDTO getLoggerAddressForUserId(@AuthenticationPrincipal LibraryUserDetails userDetails) {
        Address address = addressService.findByAddressByUserId(userDetails.userId());
        return mapping.mapToDto(address);
    }
}

