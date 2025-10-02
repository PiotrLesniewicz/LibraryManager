package org.library.integration.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.library.configuration.TestContainerConfig;
import org.library.data.DataTestFactory;
import org.library.domain.exception.NotFoundUserException;
import org.library.domain.model.*;
import org.library.domain.service.AccountUserService;
import org.library.domain.service.AddressService;
import org.library.domain.service.LibrarianService;
import org.library.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yaml")
@AllArgsConstructor(onConstructor_ = @Autowired)
class AccountUserServiceIntegrationTest extends TestContainerConfig {

    private AccountUserService accountUserService;
    private UserService userService;
    private AddressService addressService;
    private LibrarianService librarianService;
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateNewUser_WhenUserDoesNotExist() {
        // phone number is empty because it is optional
        // membershipDate is empty because it is set during newUser creation

        // given
        Long initCountUser = userService.countUser();
        User newUser = DataTestFactory.userForNewAccount();
        assertThat(userService.findUserByEmail(newUser.getEmail())).isEmpty();

        // when
        User savedUser = accountUserService.createAccountUser(newUser);

        // then
        Long updateCountUser = userService.countUser();
        assertThat(initCountUser + 1).isEqualTo(updateCountUser);


        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getAddress().getAddressId()).isNotNull();
        assertThat(savedUser.getMembershipDate()).isNotNull();

        assertThat(savedUser).usingRecursiveComparison()
                .ignoringFields("userId", "password", "membershipDate", "address.addressId", "address.users")
                .isEqualTo(newUser);
        assertThat(passwordEncoder.matches(newUser.getPassword(), savedUser.getPassword())).isTrue();
    }

    @Test
    void shouldCreateNewUser_WhenAddressExists() {
        // given
        Address addressExistInDB = addressService.findAddressById(1);
        int initCountUsersForAddress = userService.findCountUsersForAddress(addressExistInDB.getAddressId());
        User user = DataTestFactory.userWithExistingAddress(addressExistInDB);

        // when
        User savedUser = accountUserService.createAccountUser(user);

        // then
        assertThat(savedUser.getAddress()).isEqualTo(addressExistInDB);
        int updateCountUsersForAddress = userService.findCountUsersForAddress(addressExistInDB.getAddressId());
        assertThat(initCountUsersForAddress + 1).isEqualTo(updateCountUsersForAddress);
    }

    @Test
    void shouldCreateLibrarian_WhenSavingNewUserWithLibrarianRole() {
        // given
        //hireDate is empty because it is set during newUser creation
        User newUser = DataTestFactory.librarianUser();

        Long initCountLibrarian = librarianService.countLibrarian();

        // when
        User savedUser = accountUserService.createAccountUser(newUser);

        // then
        assertThat(savedUser.getUserRole()).isEqualTo(UserRole.LIBRARIAN);
        assertThat(savedUser.getLibrarian().getLibrarianRole()).isEqualTo(LibrarianRole.ADMIN);
        assertThat(savedUser.getLibrarian().getHireDate()).isNotNull();
        Long updateCountLibrarian = librarianService.countLibrarian();
        assertThat(updateCountLibrarian).isEqualTo(initCountLibrarian + 1);
    }

    @Test
    void shouldCreateLibrarian_WhenUpdatingUserWithLibrarianRole() {
        // given
        String email = "bob.williams4@example.com";
        User existingUser = userService.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(email)));

        assertThat(librarianService.findByUserId(existingUser.getUserId())).isEmpty();
        Long initCountLibrarian = librarianService.countLibrarian();
        User updateUser = existingUser.withUserRole(UserRole.LIBRARIAN).withLibrarian(Librarian.builder().librarianRole(LibrarianRole.TECHNIC).build());

        // when
        User savedUser = accountUserService.updateAccountUser(updateUser);

        // then
        Long updateCountLibrarian = librarianService.countLibrarian();
        assertThat(savedUser.getUserRole()).isEqualTo(UserRole.LIBRARIAN);
        assertThat(savedUser.getLibrarian().getLibrarianRole()).isEqualTo(LibrarianRole.TECHNIC);
        assertThat(savedUser.getLibrarian().getHireDate()).isNotNull();
        assertThat(initCountLibrarian + 1).isEqualTo(updateCountLibrarian);
    }

    @Test
    void shouldCorrectlyUpdateAddress_WhenExistingUserHasNewAddress() {
        // given
        String email = "jane.smith2@example.com";
        User existingUser = userService.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(email)));


        Integer addressId = existingUser.getAddress().getAddressId();
        int initCountUsersForAddress = userService.findCountUsersForAddress(addressId);

        Address newAddress = DataTestFactory.differentAddress();

        User userUpdate = existingUser.withAddress(newAddress);

        // when
        User savedUser = accountUserService.updateAccountUser(userUpdate);

        //then
        assertThat(savedUser.getUserId()).isEqualTo(userUpdate.getUserId());
        assertThat(savedUser.getEmail()).isEqualTo(userUpdate.getEmail());
        assertThat(savedUser.getAddress().getAddressId()).isNotEqualTo(addressId);
        assertThat(savedUser.getAddress().getCity()).isEqualTo(newAddress.getCity());

        int updateCountUsersForAddress = userService.findCountUsersForAddress(addressId);
        int countUsersForNewAddress = userService.findCountUsersForAddress(savedUser.getAddress().getAddressId());
        assertThat(initCountUsersForAddress - 1).isEqualTo(updateCountUsersForAddress);
        assertThat(countUsersForNewAddress).isEqualTo(1);
    }

    @Test
    void shouldCorrectlyUpdateAccountUser_WhenAddressIsTheSame() {
        // given
        String oldEmail = "noah.clark16@example.com";
        String newEmail = "example@wp.pl";
        String newUserName = "exampleUserName";
        User existingUser = userService.findUserByEmail(oldEmail).orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(oldEmail)));

        int initCountUsersForAddress = userService.findCountUsersForAddress(existingUser.getAddress().getAddressId());

        assertThat(userService.findUserByEmail(newEmail)).isEmpty();
        assertThat(userService.findUserByUserName(newUserName)).isEmpty();

        User updateUser = existingUser.withEmail(newEmail).withUserName(newUserName);

        // when
        User savedUser = accountUserService.updateAccountUser(updateUser);

        // then
        assertThat(savedUser.getUserId()).isEqualTo(updateUser.getUserId());
        assertThat(savedUser.getUserName()).isEqualTo(updateUser.getUserName());
        assertThat(savedUser.getEmail()).isEqualTo(updateUser.getEmail());
        assertThat(savedUser.getMembershipDate()).isEqualTo(updateUser.getMembershipDate());

        int updateCountUsersForAddress = userService.findCountUsersForAddress(savedUser.getAddress().getAddressId());
        assertThat(initCountUsersForAddress).isEqualTo(updateCountUsersForAddress);

        assertThat(addressService.findByAddressByUserId(savedUser.getUserId()).getUsers()).extracting(User::getEmail).doesNotContain(existingUser.getEmail()).contains(savedUser.getEmail());
        assertThat(addressService.findByAddressByUserId(savedUser.getUserId()).getUsers()).extracting(User::getUserName).doesNotContain(existingUser.getUserName()).contains(savedUser.getUserName());
    }

    @Test
    void shouldCorrectlyDeleteUserWithLibrarian() {
        // given
        String email = "frank.wilson8@example.com";
        User existingUser = userService.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(email)));
        assertThat(existingUser.getAddress().getUsers()).contains(existingUser);

        // when
        accountUserService.deleteAccountUser(existingUser);

        // then
        assertThat(userService.findUserByEmail(email)).isEmpty();

        Set<User> usersForAddress = addressService.findAddressById(existingUser.getAddress().getAddressId()).getUsers();

        assertThat(usersForAddress).isNotEmpty().doesNotContain(existingUser);
        Integer userId = existingUser.getUserId();
        assertThat(librarianService.findByUserId(userId)).isEmpty();

    }

    @Test
    void shouldCorrectlyRemoveLibrarian() {
        // given
        String email = "jack.thomas12@example.com";
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("User with email [%s] dose not exist".formatted(email)));

        assertThat(user.getLibrarian()).isNotNull();

        // when
        accountUserService.removeLibrarian(user);

        // then
        User userWithoutLibrarian = userService.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found after update"));

        assertThat(userWithoutLibrarian.getLibrarian()).isNull();
        assertThat(userWithoutLibrarian.getUserRole()).isEqualTo(UserRole.USER);
    }
}
