package org.library.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.data.DataTestFactory;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.User;
import org.library.domain.service.AccountUserService;
import org.library.domain.service.AddressManager;
import org.library.domain.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountUserServiceUnitTest {

    @InjectMocks
    private AccountUserService accountUserService;
    @Mock
    private UserService userService;
    @Mock
    private AddressManager addressManager;

    @Test
    void shouldThrowException_WhenUserIsNull() {
        // given, when, then
        assertThatThrownBy(() -> accountUserService.accountUser(null))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("The user and address must not be empty!");
    }

    @Test
    void shouldThrowException_WhenUserHasNoAddress() {
        // given
        User user = DataTestFactory.defaultUser();
        assertThat(user.getAddress()).isNull();

        // when, then
        assertThatThrownBy(() -> accountUserService.accountUser(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("The user and address must not be empty!");
    }

    @Test
    void shouldThrowException_WhenEmailIsUsedByAnotherUser() {
        // given
        String email = "exist@gmail.com";
        User exist = DataTestFactory.defaultUser()
                .withUserId(10)
                .withEmail(email)
                .withAddress(DataTestFactory.defaultAddress());
        User newUser = DataTestFactory.differentUser()
                .withUserId(null)
                .withEmail(email)
                .withAddress(DataTestFactory.defaultAddress());

        Mockito.when(userService.findUserByEmail(Mockito.eq(email)))
                .thenReturn(Optional.of(exist));

        // when, then
        Assertions.assertThatThrownBy(() -> accountUserService.accountUser(newUser))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Email is already in use by another user.");
    }
}