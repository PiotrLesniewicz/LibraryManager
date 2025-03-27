package org.library.domain.service;

import lombok.AllArgsConstructor;
import org.library.domain.exception.AddressMissingException;
import org.library.domain.model.Address;
import org.library.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AddressResolver {

    private final AddressService addressService;

    @Transactional
    public Address findOrSaveAddress(User user) {
        Address address = user.getAddress();

        return Objects.isNull(address.getAddressId()) ?
                findAddressByFieldsOrSave(address)
                : findAddressById(address.getAddressId());
    }

    @Transactional
    public Address updateOrSaveAddress(User user) {
        Address address = user.getAddress();
        if (Objects.isNull(address.getAddressId())) {
            return addressService.saveAddress(user.getAddress());
        }

        Address existingAddress = findAddressById(address.getAddressId());
        if (shouldCreateNewAddress(user, existingAddress)) {
            Address newAddress = buildAddress(user);
            addressService.removeUserForAddress(address.getAddressId(), user);
            return findAddressByFieldsOrSave(newAddress);
        }
        return existingAddress;

    }

    private boolean shouldCreateNewAddress(User user, Address address) {
        return address.getUsers().size() > 1 && isAddressChanged(address, user);
    }

    private Address findAddressByFieldsOrSave(Address address) {
        return addressService.findAddressByCityAndStreetAndNumberAndPostCode(address)
                .orElseGet(() -> addressService.saveAddress(address));
    }

    private Address findAddressById(Integer addressId) {
        return addressService.findAddressById(addressId)
                .orElseThrow(() -> new AddressMissingException("No address found for the address id: [%s]"
                        .formatted(addressId)));
    }

    private boolean isAddressChanged(Address address, User user) {
        return !address.getCity().equals(user.getAddress().getCity())
                || !address.getStreet().equals(user.getAddress().getStreet())
                || !address.getNumber().equals(user.getAddress().getNumber())
                || !address.getPostCode().equals(user.getAddress().getPostCode());
    }

    private Address buildAddress(User user) {
        return Address.builder()
                .city(user.getAddress().getCity())
                .street(user.getAddress().getStreet())
                .number(user.getAddress().getNumber())
                .postCode(user.getAddress().getPostCode())
                .build();
    }
}
