package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.model.Address;
import org.library.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AddressManager {

    private final AddressService addressService;

    @Transactional
    public Address findOrCreateNewAddress(User user) {
        Address address = user.getAddress();
        return Objects.isNull(address.getAddressId()) ?
                findAddressByFieldsOrSave(address)
                : addressService.findAddressById(address.getAddressId());
    }

    @Transactional
    public Address updateOrCreateNewAddress(User user) {
        Integer addressId = user.getAddress().getAddressId();
        return Objects.isNull(addressId)
                ? handleAddressWithoutId(user)
                : handleExistingAddress(user);
    }

    private Address handleAddressWithoutId(User user) {
        dissociateUserFromCurrentAddress(user);
        return findAddressByFieldsOrSave(user.getAddress());
    }

    private Address handleExistingAddress(User user) {
        Address existingAddress = addressService.findAddressById(user.getAddress().getAddressId());
        if (!isAddressChanged(existingAddress, user)) {
            return existingAddress;
        }
        dissociateUserFromCurrentAddress(user);
        Address newAddress = buildAddress(user);
        return findAddressByFieldsOrSave(newAddress);
    }

    private Address findAddressByFieldsOrSave(Address address) {
        return addressService.findAddressByCityAndStreetAndNumberAndPostCode(address)
                .orElseGet(() -> addressService.saveAddress(address));
    }

    @Transactional
    public void addUserToAddress(User user, Address address) {
        Set<User> users = new HashSet<>(address.getUsers());
        users.add(user);
        addressService.saveAddress(address.withUsers(users));
    }

    @Transactional
    public void dissociateUserFromCurrentAddress(User user) {
        Integer userId = user.getUserId();
        Address address = addressService.findByAddressByUserId(userId);
        Set<User> users = new HashSet<>(address.getUsers());
        users.remove(user);
        addressService.saveAddress(address.withUsers(users));
    }

    private boolean isAddressChanged(Address address, User user) {
        return !address.getCity().equals(user.getAddress().getCity())
                || !address.getStreet().equals(user.getAddress().getStreet())
                || !address.getNumber().equals(user.getAddress().getNumber())
                || !address.getPostCode().equals(user.getAddress().getPostCode());
    }

    private Address buildAddress(User user) {
        return Address.builder()
                .addressId(null)
                .city(user.getAddress().getCity())
                .street(user.getAddress().getStreet())
                .number(user.getAddress().getNumber())
                .postCode(user.getAddress().getPostCode())
                .build();
    }
}
