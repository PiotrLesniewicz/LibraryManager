package org.library.unit;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.data.DataTestFactory;
import org.library.domain.exception.NotFoundAddressException;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Address;
import org.library.domain.model.User;
import org.library.domain.service.AddressService;
import org.library.infrastructure.database.entity.AddressEntity;
import org.library.infrastructure.database.repository.AddressRepository;
import org.library.infrastructure.mapper.AddressEntityMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressEntityMapper addressEntityMapper;

    @Test
    void shouldThrowException_WhenAddressIsNull() {
        // when, then
        Assertions.assertThatThrownBy(() -> addressService.createOrFindAddress(null))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("The address must not be empty.");
    }

    @Test
    void shouldReturnExistingAddressWithoutSaving_WhenAddressAlreadyExists() {
        // given
        Address address = DataTestFactory.defaultAddress();
        Address existingDB = address.withAddressId(1);
        AddressEntity addressEntity = new AddressEntity();
        when(addressRepository.findByCityAndStreetAndNumberAndPostCode(
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getPostCode()
        ))
                .thenReturn(Optional.of(addressEntity));
        when(addressEntityMapper.mapFromEntity(addressEntity)).thenReturn(existingDB);

        // when
        var result = addressService.createOrFindAddress(address);

        // then
        Assertions.assertThat(result).isEqualTo(existingDB);
        verify(addressRepository).findByCityAndStreetAndNumberAndPostCode(
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getPostCode()
        );
        verify(addressRepository, never()).save(any(AddressEntity.class));
    }

    @Test
    void shouldSaveAndReturnNewAddress_WhenAddressDoesNotExist() {
        // given
        Address address = DataTestFactory.defaultAddress();
        Address savedAddress = address.withAddressId(1);
        AddressEntity entity = new AddressEntity();

        when(addressRepository.findByCityAndStreetAndNumberAndPostCode(
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getPostCode()
        )).thenReturn(Optional.empty());

        when(addressEntityMapper.mapToEntity(any(Address.class))).thenReturn(entity);
        when(addressRepository.saveAndFlush(entity)).thenReturn(entity);
        when(addressEntityMapper.mapFromEntity(entity)).thenReturn(savedAddress);

        // when
        Address result = addressService.createOrFindAddress(address);

        // then
        Assertions.assertThat(result).isEqualTo(savedAddress);
        verify(addressRepository).findByCityAndStreetAndNumberAndPostCode(
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getPostCode()
        );
        verify(addressRepository).saveAndFlush(any(AddressEntity.class));
    }


    @Test
    void shouldReturnTheSameAddress_WhenAddressIsNotChanged() {
        // given
        Integer userId = 200;
        Address existing = DataTestFactory.defaultAddress().withAddressId(1);

        when(addressRepository.findByUserId(userId)).thenReturn(Optional.of(new AddressEntity()));
        when(addressEntityMapper.mapFromEntity(any(AddressEntity.class))).thenReturn(existing);

        // when
        Address result = addressService.updateAddress(userId,existing);

        // then
        Assertions.assertThat(result).isEqualTo(existing);
        verify(addressRepository, never()).findByCityAndStreetAndNumberAndPostCode(
                anyString(), anyString(), anyString(), anyString());
        verify(addressRepository, never()).saveAndFlush(any(AddressEntity.class));
    }

    @Test
    void shouldReturnExistingAddress_WhenAddressIsChangedButAlreadyExists() {
        // given
        Integer userId = 500;
        Address existing = DataTestFactory.defaultAddress().withAddressId(1);
        Address updated = existing.withCity("NewCity");
        Address existingInDB = updated.withAddressId(2);

        AddressEntity existingEntity = AddressEntity.builder().addressId(1).build();
        AddressEntity existingInDBEntity = AddressEntity.builder().addressId(2).build();

        when(addressEntityMapper.mapFromEntity(existingEntity))
                .thenReturn(existing);
        when(addressEntityMapper.mapFromEntity(existingInDBEntity))
                .thenReturn(existingInDB);

        when(addressRepository.findByUserId(userId))
                .thenReturn(Optional.of(existingEntity));
        when(addressRepository.findByCityAndStreetAndNumberAndPostCode(
                updated.getCity(),
                updated.getStreet(),
                updated.getNumber(),
                updated.getPostCode()
        )).thenReturn(Optional.of(existingInDBEntity));

        // when
        Address result = addressService.updateAddress(userId,updated);

        // then
        Assertions.assertThat(result).isEqualTo(existingInDB);
        verify(addressRepository).findByUserId(userId);
        verify(addressRepository).findByCityAndStreetAndNumberAndPostCode(
                updated.getCity(),
                updated.getStreet(),
                updated.getNumber(),
                updated.getPostCode()
        );
        verify(addressRepository, never()).saveAndFlush(any(AddressEntity.class));
    }

    @Test
    void shouldCreateNewAddress_WhenAddressIsChangedAndExistingHasMultipleUsers() {
        // given
        // Create an existing address with ID=1 that is shared by 2 users
        Address existing = DataTestFactory.defaultAddress()
                .withAddressId(1)
                .withCity("OldCity");

        User user1 = User.builder().userId(1).build();
        User user2 = User.builder().userId(2).build();
        existing = existing.withUsers(Set.of(user1, user2)); // Address is shared by multiple users

        // Update the address by changing the city
        Address updated = existing.withCity("NewCity");

        // Expected result: new address with new ID=3, no users associated yet
        Address savedAddress = updated.withAddressId(3).withUsers(new HashSet<>());

        // Mock entities for existing address (ID=1) and new address (ID=3)
        AddressEntity existingEntity = AddressEntity.builder()
                .addressId(1)
                .build();

        AddressEntity savedEntity = AddressEntity.builder()
                .addressId(3)
                .build();

        // Mock: finding existing address by ID returns the shared address
        when(addressRepository.findByUserId(user1.getUserId()))
                .thenReturn(Optional.of(existingEntity));
        when(addressEntityMapper.mapFromEntity(existingEntity))
                .thenReturn(existing);

        // Mock: the updated address doesn't exist in database yet
        when(addressRepository.findByCityAndStreetAndNumberAndPostCode(
                updated.getCity(),
                updated.getStreet(),
                updated.getNumber(),
                updated.getPostCode()
        )).thenReturn(Optional.empty());

        // Mock: saving the new address
        when(addressEntityMapper.mapToEntity(any(Address.class)))
                .thenReturn(savedEntity);
        when(addressRepository.saveAndFlush(savedEntity))
                .thenReturn(savedEntity);
        when(addressEntityMapper.mapFromEntity(savedEntity))
                .thenReturn(savedAddress);

        // when
        Address result = addressService.updateAddress(user1.getUserId(), updated);

        // then
        // Verify that a new address was created instead of updating the existing one
        Assertions.assertThat(result).isEqualTo(savedAddress);
        Assertions.assertThat(result.getAddressId()).isEqualTo(3); // New ID assigned
        Assertions.assertThat(result.getUsers()).isEmpty(); // No users initially

        // Verify repository interactions
        verify(addressRepository).findByUserId(user1.getUserId());
        verify(addressRepository).findByCityAndStreetAndNumberAndPostCode(
                updated.getCity(),
                updated.getStreet(),
                updated.getNumber(),
                updated.getPostCode()
        );
        verify(addressRepository).saveAndFlush(savedEntity);

        // Verify that the address was saved without ID and without users
        // (to avoid modifying the shared address used by other users)
        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressEntityMapper).mapToEntity(addressCaptor.capture());
        Address capturedAddress = addressCaptor.getValue();
        Assertions.assertThat(capturedAddress.getAddressId()).isNull(); // No ID means new record
        Assertions.assertThat(capturedAddress.getUsers()).isEmpty(); // No users to avoid conflicts
    }

    @Test
    void shouldThrowException_WhenDoseNotFindAddressById() {
        // given
        Integer addressId = 10;
        when(addressRepository.findById(addressId))
                .thenReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> addressService.findAddressById(addressId))
                .isInstanceOf(NotFoundAddressException.class)
                .hasMessage("No address found for the address id: [%s]".formatted(addressId));

        verify(addressRepository, times(1)).findById(addressId);
    }
}