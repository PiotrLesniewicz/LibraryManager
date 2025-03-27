package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.AddressMissingException;
import org.library.domain.model.Address;
import org.library.domain.model.User;
import org.library.infrastructure.database.entity.AddressEntity;
import org.library.infrastructure.database.repository.AddressRepository;
import org.library.mapper.AddressEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressEntityMapper addressEntityMapper;

    @Transactional
    public Address saveAddress(Address address) {
        AddressEntity toSave = addressEntityMapper.mapToEntity(address);
        AddressEntity addressEntity = addressRepository.saveAndFlush(toSave);
        return addressEntityMapper.mapFromEntity(addressEntity);
    }

    public Optional<Address> findAddressById(Integer addressId) {
        return addressRepository.findById(addressId)
                .map(addressEntityMapper::mapFromEntity);
    }

    public Optional<Address> findAddressByCityAndStreetAndNumberAndPostCode(Address address) {
        return addressRepository
                .findByCityAndStreetAndNumberAndPostCode(address.getCity(), address.getStreet(), address.getNumber(), address.getPostCode())
                .map(addressEntityMapper::mapFromEntity);
    }

    @Transactional
    public Address addUserToExistAddress(User user, Address address) {
        Set<User> users = new HashSet<>(address.getUsers());
        users.add(user);
        return saveAddress(address.withUsers(users));
    }

    @Transactional
    public Address removeUserForAddress(Integer addressId, User user) {
        Address address = findAddressById(addressId)
                .orElseThrow(() -> new AddressMissingException("No address found for the address id: [%s]"
                        .formatted(addressId)));

        Set<User> users = new HashSet<>(address.getUsers());
        users.remove(user);
        return saveAddress(address.withUsers(users));
    }
}
