package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.NotFoundAddressException;
import org.library.domain.model.Address;
import org.library.infrastructure.database.entity.AddressEntity;
import org.library.infrastructure.database.repository.AddressRepository;
import org.library.infrastructure.mapper.AddressEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional(readOnly = true)
    public Address findAddressById(Integer addressId) {
        return addressRepository.findById(addressId)
                .map(addressEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundAddressException("No address found for the address id: [%s]"
                        .formatted(addressId)));
    }

    @Transactional(readOnly = true)
    public Address findByAddressByUserId(Integer userId) {
        return addressRepository.findByUserId(userId)
                .map(addressEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundAddressException("No address found for the user id: [%s]"
                        .formatted(userId)));
    }

    @Transactional(readOnly = true)
    public Optional<Address> findAddressByCityAndStreetAndNumberAndPostCode(Address address) {
        return addressRepository
                .findByCityAndStreetAndNumberAndPostCode(address.getCity(), address.getStreet(), address.getNumber(), address.getPostCode())
                .map(addressEntityMapper::mapFromEntity);
    }
}
