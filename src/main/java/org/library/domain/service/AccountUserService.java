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
    private final AddressService addressService;
    private final LibrarianService librarianService;

    @Transactional
    public User createAccountUser(User user) {
        Address address = addressService.createOrFindAddress(user.getAddress());
        Librarian librarian = librarianService.createOrUpdateLibrarian(user);
        User saved = userService.createUser(user, librarian, address);
        addressService.updateUserAddressAssociation(user, saved);
        return saved;
    }

    @Transactional
    public User updateAccountUser(User updated, Integer userId) {
        User existing = userService.findById(userId);
        return update(updated, existing);
    }

    @Transactional
    public User updateAccountUser(User updated, String emailOrUserName) {
        User existing = userService.findUserByEmailOrUserName(emailOrUserName);
        return update(updated, existing);
    }

    @Transactional
    public void deleteAccountUser(Integer userId) {
        User user = userService.findById(userId);
        delete(user);
    }

    @Transactional
    public void deleteAccountUser(String emailOrUserName) {
        User user = userService.findUserByEmailOrUserName(emailOrUserName);
        delete(user);
    }

    @Transactional
    public void removeLibrarian(String emailOrUserName) {
        User user = userService.findUserByEmailOrUserName(emailOrUserName);
        if (!user.getUserRole().equals(UserRole.LIBRARIAN)) {
            throw new UserValidationException("User is not a librarian. User role: [%s]".formatted(user.getUserRole()));
        }
        librarianService.deleteLibrarian(user.getLibrarian().getLibrarianId());
        userService.downgradeLibrarianToUser(user);
    }

    private void delete(User user) {
        addressService.updateUserAddressAssociation(user, null);
        if (UserRole.LIBRARIAN.equals(user.getUserRole())) {
            librarianService.deleteLibrarian(user.getLibrarian().getLibrarianId());
        }
        userService.deleteUser(user.getUserId());
    }

    private User update(User updated, User existing) {
        Address address = addressService.updateAddress(existing.getUserId(), updated.getAddress());
        Librarian librarian = librarianService.createOrUpdateLibrarian(updated);
        User saved = userService.updateUser(existing, updated, address, librarian);
        addressService.updateUserAddressAssociation(existing, saved);
        return saved;
    }
}