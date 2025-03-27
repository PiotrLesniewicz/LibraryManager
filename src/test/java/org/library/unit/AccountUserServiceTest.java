package org.library.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.data.PreparationDataService;
import org.library.domain.exception.LibrarianMissingException;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Role;
import org.library.domain.model.User;
import org.library.domain.service.AccountUserService;
import org.library.domain.service.AddressService;
import org.library.domain.service.LibrarianService;
import org.library.domain.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class AccountUserServiceTest {

    @InjectMocks
    private AccountUserService accountUserService;

    @Mock
    private AddressService addressService;

    @Mock
    private UserService userService;

    @Mock
    private LibrarianService librarianService;

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // given, when, then
        Assertions.assertThatThrownBy(() -> accountUserService.accountUser(null))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("The user and address must not empty!!");
    }

    @Test
    void shouldThrowExceptionWhenUserHasNoAddress() {
        // given
        User user = PreparationDataService.someUser1()
                .withAddress(null);
        Assertions.assertThat(user.getAddress()).isNull();

        // when, then
        Assertions.assertThatThrownBy(() -> accountUserService.accountUser(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("The user and address must not empty!!");
    }

    @Test
    void shouldThrowExceptionWhenUserHasLibrariansSizeNotEqualsOne(){
        // given
        User user = PreparationDataService.someUser1()
                .withRole(Role.LIBRARIAN)
                .withLibrarians(Set.of());

        Assertions.assertThat(user.getLibrarians()).isEmpty();

        // when, then
        Assertions.assertThatThrownBy(() -> accountUserService.accountUser(user))
                .isInstanceOf(LibrarianMissingException.class)
                .hasMessage("The user must have exactly one librarian. Current size of librarians: [%s]."
                        .formatted(user.getLibrarians().size()));

    }

}