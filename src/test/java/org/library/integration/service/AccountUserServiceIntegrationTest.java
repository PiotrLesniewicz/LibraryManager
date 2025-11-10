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
        long initialUserCount = userService.countUser();
        User newUser = DataTestFactory.userForNewAccount();

        assertThat(userService.findUserByEmail(newUser.getEmail())).isEmpty();

        // when
        User savedUser = accountUserService.createAccountUser(newUser);

        // then
        long updatedUserCount = userService.countUser();
        assertThat(updatedUserCount).isEqualTo(initialUserCount + 1);

        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getAddress().getAddressId()).isNotNull();
        assertThat(savedUser.getMembershipDate()).isNotNull();

        assertThat(savedUser).usingRecursiveComparison()
                .ignoringFields("userId", "password", "membershipDate", "address.addressId", "address.users")
                .isEqualTo(newUser);
        assertThat(passwordEncoder.matches(newUser.getPassword(), savedUser.getPassword())).isTrue();
    }

    @Test
    void shouldReuseExistingAddress_WhenAddressAlreadyExists() {
        // given
        Address existingAddress = addressService.findAddressById(1);
        int initialUsersCountForAddress = userService.findCountUsersForAddress(existingAddress.getAddressId());
        User newUser = DataTestFactory.userWithExistingAddress(existingAddress);

        // when
        User savedUser = accountUserService.createAccountUser(newUser);

        // then
        assertThat(savedUser.getAddress()).isEqualTo(existingAddress);

        int updatedUsersCountForAddress = userService.findCountUsersForAddress(existingAddress.getAddressId());
        assertThat(updatedUsersCountForAddress).isEqualTo(initialUsersCountForAddress + 1);
    }

    @Test
    void shouldCreateLibrarianRecord_WhenCreatingUserWithLibrarianRole() {
        // hireDate is empty because it is set during newUser creation

        // given
        User newLibrarianUser = DataTestFactory.librarianUser();
        long initialLibrarianCount = librarianService.countLibrarian();

        // when
        User savedUser = accountUserService.createAccountUser(newLibrarianUser);

        // then
        assertThat(savedUser.getUserRole()).isEqualTo(UserRole.LIBRARIAN);
        assertThat(savedUser.getLibrarian().getLibrarianRole()).isEqualTo(LibrarianRole.ADMIN);
        assertThat(savedUser.getLibrarian().getHireDate()).isNotNull();

        long updatedLibrarianCount = librarianService.countLibrarian();
        assertThat(updatedLibrarianCount).isEqualTo(initialLibrarianCount + 1);
    }

    @Test
    void shouldCreateLibrarianRecord_WhenUpdatingUserToLibrarianRole() {
        // given
        String userEmail = "bob.williams4@example.com";
        User existingUser = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(userEmail)));

        assertThat(librarianService.findByUserId(existingUser.getUserId())).isEmpty();
        long initialLibrarianCount = librarianService.countLibrarian();

        User updatedUser = existingUser
                .withUserRole(UserRole.LIBRARIAN)
                .withLibrarian(Librarian.builder().librarianRole(LibrarianRole.TECHNIC).build());

        // when
        User savedUser = accountUserService.updateAccountUser(updatedUser, userEmail);

        // then
        assertThat(savedUser.getUserRole()).isEqualTo(UserRole.LIBRARIAN);
        assertThat(savedUser.getLibrarian().getLibrarianRole()).isEqualTo(LibrarianRole.TECHNIC);
        assertThat(savedUser.getLibrarian().getHireDate()).isNotNull();

        long updatedLibrarianCount = librarianService.countLibrarian();
        assertThat(updatedLibrarianCount).isEqualTo(initialLibrarianCount + 1);
    }

    @Test
    void shouldUpdateAddressInPlace_WhenUserIsOnlyOneUsingAddress() {
        // given
        String userEmail = "grace.moore9@example.com";
        User existingUser = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(userEmail)));

        Integer addressId = existingUser.getAddress().getAddressId();
        int initialUsersCountForAddress = userService.findCountUsersForAddress(addressId);
        assertThat(initialUsersCountForAddress).isEqualTo(1);

        Address updatedAddressData = existingUser.getAddress()
                .withCity("SoloCity")
                .withStreet("SoloStreet")
                .withNumber("77A")
                .withPostCode("77-777");

        User userWithUpdatedAddress = existingUser.withAddress(updatedAddressData);

        // when
        User savedUser = accountUserService.updateAccountUser(userWithUpdatedAddress, userEmail);

        // then
        assertThat(savedUser.getAddress().getAddressId()).isEqualTo(addressId);
        assertThat(savedUser.getAddress().getCity()).isEqualTo("SoloCity");

        int updatedUsersCountForAddress = userService.findCountUsersForAddress(addressId);
        assertThat(updatedUsersCountForAddress).isEqualTo(1);
    }

    @Test
    void shouldCreateNewAddress_WhenUserUpdatesSharedAddress() {
        // given: two users sharing the same address
        String user1Email = "charlie.brown5@example.com";
        String user2Email = "noah.clark16@example.com";

        User user1 = userService.findUserByEmail(user1Email)
                .orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(user1Email)));
        User user2 = userService.findUserByEmail(user2Email)
                .orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(user2Email)));

        Address sharedAddress = user1.getAddress();
        Integer sharedAddressId = sharedAddress.getAddressId();

        assertThat(user2.getAddress().getAddressId()).isEqualTo(sharedAddressId);
        int initialUsersCountForSharedAddress = sharedAddress.getUsers().size();
        assertThat(initialUsersCountForSharedAddress).isEqualTo(2);

        Address updatedAddressData = sharedAddress
                .withCity("NewCity")
                .withStreet("NewStreet")
                .withNumber("99B")
                .withPostCode("99-999");

        User user1WithNewAddress = user1.withAddress(updatedAddressData);

        // when: user1 updates their address
        User savedUser1 = accountUserService.updateAccountUser(user1WithNewAddress, user1Email);

        // then: user1 has a new address
        Address newAddress = addressService.findAddressById(savedUser1.getAddress().getAddressId());

        assertThat(newAddress.getAddressId()).isNotEqualTo(sharedAddressId);
        assertThat(newAddress.getCity()).isEqualTo("NewCity");
        assertThat(newAddress.getUsers()).containsExactly(savedUser1);

        // and: user2 still has the shared address
        User user2AfterUpdate = userService.findUserByEmail(user2Email).orElseThrow();
        assertThat(user2AfterUpdate.getAddress().getAddressId()).isEqualTo(sharedAddressId);

        Address sharedAddressAfterUpdate = addressService.findAddressById(sharedAddressId);
        assertThat(sharedAddressAfterUpdate.getUsers()).contains(user2AfterUpdate);
        assertThat(sharedAddressAfterUpdate.getUsers()).doesNotContain(savedUser1);
    }

    @Test
    void shouldUpdateUserData_WhenAddressRemainsUnchanged() {
        // given
        String oldEmail = "quinn.hall19@example.com";
        String newEmail = "example@wp.pl";
        String newUserName = "exampleUserName";

        User existingUser = userService.findUserByEmail(oldEmail)
                .orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(oldEmail)));

        Integer addressId = existingUser.getAddress().getAddressId();
        int initialUsersCountForAddress = userService.findCountUsersForAddress(addressId);

        assertThat(userService.findUserByEmail(newEmail)).isEmpty();
        assertThat(userService.findUserByUserName(newUserName)).isEmpty();

        User updatedUser = existingUser
                .withEmail(newEmail)
                .withUserName(newUserName);

        // when
        User savedUser = accountUserService.updateAccountUser(updatedUser, existingUser.getUserId());

        // then
        assertThat(savedUser.getUserId()).isEqualTo(updatedUser.getUserId());
        assertThat(savedUser.getUserName()).isEqualTo(newUserName);
        assertThat(savedUser.getEmail()).isEqualTo(newEmail);
        assertThat(savedUser.getMembershipDate()).isEqualTo(updatedUser.getMembershipDate());

        int updatedUsersCountForAddress = userService.findCountUsersForAddress(addressId);
        assertThat(updatedUsersCountForAddress).isEqualTo(initialUsersCountForAddress);

        Address addressAfterUpdate = addressService.findAddressById(addressId);
        assertThat(addressAfterUpdate.getUsers())
                .extracting(User::getEmail)
                .doesNotContain(oldEmail)
                .contains(newEmail);
        assertThat(addressAfterUpdate.getUsers())
                .extracting(User::getUserName)
                .doesNotContain(existingUser.getUserName())
                .contains(newUserName);
    }

    @Test
    void shouldDeleteUserAndLibrarianRecord_WhenUserIsLibrarian() {
        // given
        String userEmail = "frank.wilson8@example.com";
        User existingUser = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Expected test user [%s] not found. Check Flyway migration.".formatted(userEmail)));

        Integer addressId = existingUser.getAddress().getAddressId();
        Integer userId = existingUser.getUserId();

        assertThat(existingUser.getAddress().getUsers()).contains(existingUser);

        // when
        accountUserService.deleteAccountUser(existingUser.getUserId());

        // then
        assertThat(userService.findUserByEmail(userEmail)).isEmpty();

        Set<User> usersForAddressAfterDelete = addressService.findAddressById(addressId).getUsers();
        assertThat(usersForAddressAfterDelete)
                .isNotEmpty()
                .doesNotContain(existingUser);

        assertThat(librarianService.findByUserId(userId)).isEmpty();
    }

    @Test
    void shouldRemoveLibrarianRoleAndRecord_WhenChangingLibrarianToRegularUser() {
        // given
        String userEmail = "jack.thomas12@example.com";
        User existingLibrarian = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundUserException("User with email [%s] does not exist".formatted(userEmail)));

        assertThat(existingLibrarian.getLibrarian()).isNotNull();

        // when
        accountUserService.removeLibrarian(userEmail);

        // then
        User userAfterLibrarianRemoval = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found after update"));

        assertThat(userAfterLibrarianRemoval.getLibrarian()).isNull();
        assertThat(userAfterLibrarianRemoval.getUserRole()).isEqualTo(UserRole.USER);
    }
}