package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.NotFoundAddressException;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Address;
import org.library.domain.model.User;
import org.library.infrastructure.database.entity.AddressEntity;
import org.library.infrastructure.database.repository.AddressRepository;
import org.library.infrastructure.mapper.AddressEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressEntityMapper addressEntityMapper;

    @Transactional
    public Address createOrFindAddress(Address address) {
        if (Objects.isNull(address)) {
            throw new UserValidationException("The address must not be empty.");
        }
        Optional<Address> addressExisting = findAddressByFields(address);
        return addressExisting.orElseGet(() -> saveAddress(address));
    }

    // If the address is shared by multiple users, we create a new Address instance
    // instead of updating the existing one to avoid affecting other users.
    @Transactional
    public Address updateAddress(Integer userId, Address updated) {
        Address existing = findAddressByUserId(userId);
        Address merge = existing.updateFrom(updated);
        if (!existing.isAddressChanged(merge)) {
            return existing;
        }
        Optional<Address> addressOptional = findAddressByFields(merge);
        if (addressOptional.isPresent()) {
            return addressOptional.get();
        }
        if (existing.getUsers().size() > 1) {
            return saveAddress(merge.withAddressId(null).withUsers(new HashSet<>()));
        }
        return saveAddress(merge);
    }

    @SuppressWarnings("java:S2259")
    public void updateUserAddressAssociation(User oldUser, User newUser) {
        if (Objects.isNull(oldUser) && Objects.isNull(newUser)) {
            throw new IllegalArgumentException("Both old and new user cannot be null");
        }
        if (Objects.isNull(oldUser.getUserId())) {
            addUserToAddress(newUser);
            return;
        }
        if (Objects.isNull(newUser)) {
            removeUserFromAddress(oldUser);
            return;
        }
        Integer oldAddressId = oldUser.getAddress().getAddressId();
        Integer newAddressId = newUser.getAddress().getAddressId();
        if (!Objects.equals(oldAddressId, newAddressId)) {
            removeUserFromAddress(oldUser);
            addUserToAddress(newUser);
        }
    }

    public Address saveAddress(Address address) {
        AddressEntity toSave = addressEntityMapper.mapToEntity(address);
        AddressEntity addressEntity = addressRepository.saveAndFlush(toSave);
        return addressEntityMapper.mapFromEntity(addressEntity);
    }

    public Address findAddressById(Integer addressId) {
        return addressRepository.findById(addressId)
                .map(addressEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundAddressException("No address found for the address id: [%s]"
                        .formatted(addressId)));
    }

    private Address findAddressByUserId(Integer userId) {
        return addressRepository.findByUserId(userId)
                .map(addressEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundAddressException("No address found for the user id: [%s]"
                        .formatted(userId)));
    }

    public Optional<Address> findAddressByFields(Address address) {
        return addressRepository
                .findByCityAndStreetAndNumberAndPostCode(
                        address.getCity(),
                        address.getStreet(),
                        address.getNumber(),
                        address.getPostCode())
                .map(addressEntityMapper::mapFromEntity);
    }

    private void addUserToAddress(User user) {
        Address addedUser = user.getAddress().withAddedUser(user);
        saveAddress(addedUser);
    }

    private void removeUserFromAddress(User user) {
        Address current = user.getAddress();
        Address updated = current.withRemovedUser(user);
        if (updated.getUsers().isEmpty()) {
            addressRepository.deleteById(current.getAddressId());
        } else {
            saveAddress(updated);
        }
    }
}
