package org.library.unit;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.domain.exception.NotFoundAddressException;
import org.library.domain.service.AddressService;
import org.library.infrastructure.database.repository.AddressRepository;
import org.library.infrastructure.mapper.AddressEntityMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressEntityMapper addressEntityMapper;

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

    @Test
    void shouldThrowException_WhenDoseNotFindAddressByUserId() {
        // given
        Integer userId = 7;
        when(addressRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> addressService.findByAddressByUserId(userId))
                .isInstanceOf(NotFoundAddressException.class)
                .hasMessage("No address found for the user id: [%s]".formatted(userId));

        verify(addressRepository, times(1)).findByUserId(userId);
    }
}