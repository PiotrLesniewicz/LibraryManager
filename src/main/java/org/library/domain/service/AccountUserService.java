package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountUserService {

    private final UserService userService;
    private final AddressManager addressManager;
    private final LibrarianService librarianService;

    @Transactional
    public User createAccountUser(User user) {
        Address address = addressManager.findOrCreateNewAddress(user);
        Librarian librarian = librarianService.createOrUpdateLibrarian(user);
        User savedUser = userService.createUser(user, librarian, address);
        addressManager.addUserToAddress(savedUser, address);
        return savedUser;
    }

    @Transactional
    public User updateAccountUser(User updated) {
        User existing = userService.findById(updated.getUserId());
        Address address = addressManager.updateOrCreateNewAddress(updated);
        Librarian librarian = librarianService.createOrUpdateLibrarian(updated);
        User saved = userService.updateUser(existing, updated, address, librarian);
        if (!existing.getAddress().getAddressId().equals(address.getAddressId())) {
            addressManager.addUserToAddress(saved, address);
        }
        return saved;
    }

    @Transactional
    public void deleteAccountUser(User user) {
        addressManager.dissociateUserFromCurrentAddress(user);
        if (UserRole.LIBRARIAN.equals(user.getUserRole())) {
            librarianService.deleteLibrarian(user.getLibrarian().getLibrarianId());
        }
        userService.deleteUser(user.getUserId());
    }

    @Transactional
    public void removeLibrarian(User user) {
        if (!user.getUserRole().equals(UserRole.LIBRARIAN)){
            throw new UserValidationException("User is not a librarian. User role: [%s]".formatted(user.getUserRole()));
        }
        librarianService.deleteLibrarian(user.getLibrarian().getLibrarianId());
        userService.updateUser(user.getUserId());
    }
}