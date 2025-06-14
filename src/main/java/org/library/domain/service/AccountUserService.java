package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountUserService {

    private final UserService userService;
    private final AddressManager addressManager;
    private final LibrarianService librarianService;

    @Transactional
    public User accountUser(User user) {
        if (Objects.isNull(user) || Objects.isNull(user.getAddress())) {
            throw new UserValidationException("The user and address must not be empty!");
        }

        Optional<User> userWithEmail = userService.findUserByEmail(user.getEmail());

        if (userWithEmail.isPresent() && !userWithEmail.get().getUserId().equals(user.getUserId())) {
            throw new UserValidationException("Email is already in use by another user.");
        }

        boolean isNewUser = Objects.isNull(user.getUserId());
        return isNewUser ? createUser(user) : updateUser(user);
    }

    private User createUser(User user) {
        Address address = addressManager.findOrCreateNewAddress(user);
        return saveUserWithAddressAndLibrarian(user, address);
    }

    private User updateUser(User user) {
        Address addressUpdate = addressManager.updateOrCreateNewAddress(user);
        return saveUserWithAddressAndLibrarian(user, addressUpdate);
    }

    private User saveUserWithAddressAndLibrarian(User user, Address address) {
        User userWithAddressId = user.withAddress(address);
        User savedUser = userService.saveUser(userWithAddressId);
        addressManager.addUserToAddress(savedUser, address);
        if (UserRole.LIBRARIAN.equals(user.getUserRole()) && Objects.nonNull(user.getLibrarian())) {
            Librarian saved = librarianService.saveLibrarian(user.getLibrarian().withUser(savedUser));
            return userService.saveUser(savedUser.withLibrarian(saved));
        }
        return savedUser;
    }

    @Transactional
    public void deleteAccountUser(User user) {
        addressManager.dissociateUserFromCurrentAddress(user);
        Integer userId = user.getUserId();
        if (UserRole.LIBRARIAN.equals(user.getUserRole())) {
            Librarian librarian = librarianService.findByUserId(userId);
            librarianService.deleteLibrarian(librarian.getLibrarianId());
        }
        userService.deleteUser(userId);
    }

    @Transactional
    public void removeLibrarian(Librarian librarian) {
        librarianService.deleteLibrarian(librarian.getLibrarianId());
        userService.saveUser(librarian.getUser().withUserRole(UserRole.USER));
    }
}