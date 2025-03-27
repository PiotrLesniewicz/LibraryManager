package org.library.integration;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.library.configuration.TestContainerConfig;
import org.library.data.PreparationDataService;
import org.library.domain.model.Address;
import org.library.domain.model.Role;
import org.library.domain.model.User;
import org.library.domain.service.AccountUserService;
import org.library.domain.service.AddressService;
import org.library.domain.service.LibrarianService;
import org.library.domain.service.UserService;
import org.library.infrastructure.configuration.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@SpringJUnitConfig({ApplicationConfiguration.class})
public class AccountUserServiceTest extends TestContainerConfig {

    private AccountUserService accountUserService;
    private UserService userService;
    private AddressService addressService;
    private LibrarianService librarianService;


    @Test
    void givenUserDoesNotExist_whenAddingUser_thenUserIsPersisted() {

        // given
        Long initCountUser = userService.countUser();
        List<User> users = List.of(PreparationDataService.someUser1(), PreparationDataService.someUser2());

        for (User user : users) {
            Assertions.assertThat(userService.findUserByEmail(user.getEmail())).isEmpty();
        }

        // when
        users.forEach(accountUserService::accountUser);

        // then
        Long updateCountUser = userService.countUser();
        Assertions.assertThat(initCountUser + 2).isEqualTo(updateCountUser);

        for (User user : users) {
            User expected = userService.findUserByEmail(user.getEmail()).orElseThrow();
            Assertions.assertThat(user.getUserName()).isEqualTo(expected.getUserName());
        }
    }

    @Test
    void givenExistingAddress_whenAddingUser_thenSuccess() {
        // given
        Optional<Address> addressExistInDB = addressService.findAddressById(1);
        Assertions.assertThat(addressExistInDB).isPresent();

//        int initCountAddress = addressExistInDB.get().getUsers().size();
        User user = PreparationDataService.someUser3().withAddress(addressExistInDB.get());

        // when
        User savedUser = accountUserService.accountUser(user);


        // then

        Assertions.assertThat(addressExistInDB.get()).isEqualTo(savedUser.getAddress());
    }

    @Test
    void whenAddingUser_thenNewLibrarianIsCreatedAndAssigned() {
        // given
        User user = PreparationDataService.someUser4()
                .withRole(Role.LIBRARIAN)
                .withLibrarians(Set.of(PreparationDataService.someLibrarian1()));

        Long initCountLibrarian = librarianService.countLibrarian();

        // when
        accountUserService.accountUser(user);

        // then
        Long updateCountLibrarian = librarianService.countLibrarian();
        Assertions.assertThat(initCountLibrarian + 1).isEqualTo(updateCountLibrarian);
    }

    @Test
    void shouldCorrectlyUpdateUser() {
        // given
        Optional<User> user = userService.findUserByEmail("bob.williams4@example.com");
        Address address = PreparationDataService.someAddress1();

        Assertions.assertThat(user).isPresent();
        Long initCountLibrarian = librarianService.countLibrarian();
        User updateUser = user.get()
                .withAddress(address)
                .withRole(Role.LIBRARIAN)
                .withLibrarians(Set.of(PreparationDataService.someLibrarian2()));

        // when
        User savedUser = accountUserService.accountUser(updateUser);

        // then
        Long updateCountLibrarian = librarianService.countLibrarian();
        Assertions.assertThat(updateUser.getRole()).isEqualTo(savedUser.getRole());
        Assertions.assertThat(initCountLibrarian + 1).isEqualTo(updateCountLibrarian);
    }

    @Test
    void shouldCorrectlyUpdateAddress_WhenExistUserHaveNewAddress() {
        // given
        Optional<User> user = userService.findUserByEmail("jane.smith2@example.com");
        Assertions.assertThat(user).isPresent();

        Integer addressId = user.get().getAddress().getAddressId();
        int initCountUsersWithAddress = addressService.findAddressById(addressId)
                .map(Address::getUsers)
                .get().size();


        Address newAddress = user.get().getAddress()
                .withCity("NewCity")
                .withStreet("NewStreet")
                .withNumber("105/22")
                .withPostCode("26-260");

        User userUpdate = user.get().withAddress(newAddress);

        // when
        User savedUser = accountUserService.accountUser(userUpdate);

        //then
        Address address = addressService.findAddressById(savedUser.getAddress().getAddressId()).orElseThrow();
        Assertions.assertThat(savedUser.getAddress().getAddressId()).isEqualTo(address.getAddressId());

        int updateCountUsersWithAddress = addressService.findAddressById(addressId)
                .map(Address::getUsers).get().size();
        Assertions.assertThat(initCountUsersWithAddress - 1).isEqualTo(updateCountUsersWithAddress);
    }
}
