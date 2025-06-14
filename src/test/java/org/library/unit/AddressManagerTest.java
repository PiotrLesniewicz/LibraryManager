package org.library.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.data.DataTestFactory;
import org.library.domain.model.Address;
import org.library.domain.model.User;
import org.library.domain.service.AddressManager;
import org.library.domain.service.AddressService;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressManagerTest {

    @InjectMocks
    private AddressManager addressManager;
    @Mock
    private AddressService addressService;
    @Captor
    private ArgumentCaptor<Address> addressCaptor;


    @Test
    void shouldCreateNewAddress_WhenUserProvidesAddressWithoutIdAndItDoesNotExist() {
        // given
        User user = DataTestFactory.defaultUser().withUserId(5);
        Address newAddress = DataTestFactory.defaultAddress().withAddressId(null);
        Address savedAddress = newAddress.withAddressId(3);

        when(addressService.findAddressByCityAndStreetAndNumberAndPostCode(eq(newAddress)))
                .thenReturn(Optional.empty());
        when(addressService.saveAddress(eq(newAddress)))
                .thenReturn(savedAddress);

        // when
        Address resultAddress = addressManager.findOrCreateNewAddress(user.withAddress(newAddress));

        // then
        Assertions.assertThat(resultAddress.getAddressId()).isEqualTo(3);
        verify(addressService, times(1)).findAddressByCityAndStreetAndNumberAndPostCode(eq(newAddress));
        verify(addressService, times(1)).saveAddress(eq(newAddress));

        verifyNoMoreInteractions(addressService);
    }

    @Test
    void shouldFindExistingAddress_WhenUserProvidesAddressWithId() {
        // given
        User user = DataTestFactory.defaultUser().withUserId(8);
        int addressId = 1;
        Address existingAddress = DataTestFactory.defaultAddress()
                .withAddressId(addressId);

        when(addressService.findAddressById(eq(addressId)))
                .thenReturn(existingAddress);

        // when
        Address resultAddress = addressManager.findOrCreateNewAddress(user.withAddress(existingAddress));

        // then
        Assertions.assertThat(resultAddress.getAddressId()).isEqualTo(addressId);
        verify(addressService, times(1)).findAddressById(eq(addressId));

        verifyNoMoreInteractions(addressService);
    }

    @Test
    void shouldDissociateUserFromOldAddressAndReturnFoundAddress_WhenUpdatingToExistingAddressDetails() {
        // given
        Address newAddress = DataTestFactory.defaultAddress().withAddressId(null);
        User userUpdate = DataTestFactory.defaultUser()
                .withUserId(47)
                .withAddress(newAddress);
        Address oldUserAddress = DataTestFactory.differentAddress()
                .withAddressId(12)
                .withUsers(new HashSet<>(Set.of(userUpdate)));
        Address foundMatchingAddress = newAddress.withAddressId(17);

        when(addressService.findByAddressByUserId(eq(userUpdate.getUserId())))
                .thenReturn(oldUserAddress);
        when(addressService.saveAddress(eq(oldUserAddress)))
                .thenReturn(oldUserAddress.withUsers(Set.of()));
        when(addressService.findAddressByCityAndStreetAndNumberAndPostCode(eq(newAddress)))
                .thenReturn(Optional.of(foundMatchingAddress));

        // when
        Address resultAddress = addressManager.updateOrCreateNewAddress(userUpdate);

        // then
        Assertions.assertThat(resultAddress.getAddressId()).isEqualTo(foundMatchingAddress.getAddressId());
        verify(addressService, times(1)).findByAddressByUserId(eq(userUpdate.getUserId()));
        verify(addressService, times(1)).findAddressByCityAndStreetAndNumberAndPostCode(eq(newAddress));
        verify(addressService, times(1)).saveAddress(addressCaptor.capture());

        Address capturedOldAddress = addressCaptor.getValue();
        Assertions.assertThat(capturedOldAddress.getAddressId()).isEqualTo(oldUserAddress.getAddressId());
        Assertions.assertThat(capturedOldAddress.getUsers()).doesNotContain(userUpdate);
        Assertions.assertThat(capturedOldAddress.getUsers()).isEmpty();

        verifyNoMoreInteractions(addressService);
    }

    @Test
    void shouldFindExistingAddress_WhenAddressNotUpdateAndAddressIsTheSame() {
        // given
        User user = DataTestFactory.defaultUser()
                .withUserId(12)
                .withAddress(DataTestFactory.defaultAddress()
                        .withAddressId(5));
        Address foundAddress = user.getAddress();

        when(addressService.findAddressById(eq(user.getAddress().getAddressId())))
                .thenReturn(foundAddress);

        // when
        Address resultAddress = addressManager.updateOrCreateNewAddress(user);

        // then
        Assertions.assertThat(resultAddress.getAddressId()).isEqualTo(foundAddress.getAddressId());
        verify(addressService, times(1)).findAddressById(eq(user.getAddress().getAddressId()));

        verifyNoMoreInteractions(addressService);
    }

    @Test
    void shouldUpdateAddress_WhenAddressIsUpdateAndAddressIsDifferent() {
        // given
        User user = DataTestFactory.defaultUser()
                .withUserId(1)
                .withAddress(DataTestFactory.defaultAddress()
                        .withAddressId(10));

        Address updateAddress = DataTestFactory.differentAddress().withAddressId(user.getAddress().getAddressId());

        Address oldAddress = DataTestFactory.defaultAddress()
                .withAddressId(user.getAddress().getAddressId())
                .withUsers(Set.of(user));

        Address newAddress = updateAddress.withAddressId(null);
        Address savedAddress = newAddress.withAddressId(100);

        when(addressService.findAddressById(eq(user.getAddress().getAddressId())))
                .thenReturn(oldAddress);
        when(addressService.findByAddressByUserId(eq(user.getUserId())))
                .thenReturn(oldAddress);
        when(addressService.findAddressByCityAndStreetAndNumberAndPostCode(any(Address.class)))
                .thenReturn(Optional.empty());
        when(addressService.saveAddress(any(Address.class)))
                .thenReturn(savedAddress);

        // when
        Address resultAddress = addressManager.updateOrCreateNewAddress(user.withAddress(updateAddress));

        // then
        Assertions.assertThat(resultAddress.getAddressId()).isEqualTo(savedAddress.getAddressId());

        verify(addressService, times(1)).findAddressById(eq(user.getAddress().getAddressId()));
        verify(addressService, times(1)).findByAddressByUserId(eq(user.getUserId()));
        verify(addressService, times(2)).saveAddress(addressCaptor.capture());

        var savedAddresses = addressCaptor.getAllValues();
        Address firstSaved = savedAddresses.get(0);
        Address secondSaved = savedAddresses.get(1);

        Assertions.assertThat(firstSaved.getAddressId()).isEqualTo(oldAddress.getAddressId());
        Assertions.assertThat(firstSaved.getUsers()).doesNotContain(user);
        Assertions.assertThat(firstSaved.getUsers()).isEmpty();

        Assertions.assertThat(secondSaved.getCity()).isEqualTo(updateAddress.getCity());
        Assertions.assertThat(secondSaved.getStreet()).isEqualTo(updateAddress.getStreet());
        Assertions.assertThat(secondSaved.getAddressId()).isNull();

        verifyNoMoreInteractions(addressService);
    }


    @Test
    void shouldCorrectlyAddUserToAddress() {
        // given
        User userExist = DataTestFactory.defaultUser().withUserId(55);
        Address address = DataTestFactory.defaultAddress()
                .withAddressId(20)
                .withUsers(Set.of(userExist));
        User newUser = DataTestFactory.defaultUser().withUserId(74);

        when(addressService.saveAddress(any(Address.class)))
                .thenReturn(address.withUsers(Set.of(userExist, newUser)));

        // when
        addressManager.addUserToAddress(newUser, address);

        // then
        verify(addressService, times(1)).saveAddress(addressCaptor.capture());

        Address captured = addressCaptor.getValue();
        Assertions.assertThat(captured.getAddressId()).isEqualTo(address.getAddressId());
        Assertions.assertThat(captured.getUsers()).containsExactlyInAnyOrder(userExist, newUser);
        Assertions.assertThat(captured.getUsers()).hasSize(2);

        verifyNoMoreInteractions(addressService);
    }

    @Test
    void shouldCorrectlyDissociateUserFromAddress() {
        // given
        User deletingUser = DataTestFactory.defaultUser().withUserId(1);
        User user2 = DataTestFactory.defaultUser().withUserId(2);
        User user3 = DataTestFactory.defaultUser().withUserId(3);

        Set<User> usersForAddress = new HashSet<>(Set.of(deletingUser, user2, user3));
        Address address = DataTestFactory.defaultAddress()
                .withAddressId(1)
                .withUsers(usersForAddress);

        when(addressService.findByAddressByUserId(eq(deletingUser.getUserId())))
                .thenReturn(address);

        when(addressService.saveAddress(any(Address.class)))
                .thenReturn(null);

        // when
        addressManager.dissociateUserFromCurrentAddress(deletingUser.withAddress(address));

        // then

        verify(addressService, times(1)).findByAddressByUserId(deletingUser.getUserId());
        verify(addressService, times(1)).saveAddress(addressCaptor.capture());

        Address captured = addressCaptor.getValue();
        Assertions.assertThat(captured.getAddressId()).isEqualTo(address.getAddressId());
        Assertions.assertThat(captured.getUsers()).containsExactlyInAnyOrder(user2, user3);
        Assertions.assertThat(captured.getUsers()).doesNotContain(deletingUser);
        Assertions.assertThat(captured.getUsers()).hasSize(2);

        verifyNoMoreInteractions(addressService);
    }

}