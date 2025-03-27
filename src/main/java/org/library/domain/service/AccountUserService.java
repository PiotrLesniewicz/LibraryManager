package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.Role;
import org.library.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountUserService {

    private final UserService userService;
    private final AddressService addressService;
    private final AddressResolver addressResolver;
    private final LibrarianService librarianService;


    @Transactional
    public User accountUser(User user) {
        if (Objects.isNull(user) || Objects.isNull(user.getAddress())) {
            throw new UserValidationException("The user and address must not be empty!!");
        }
        boolean isNewUser = userService.findUserByEmail(user.getEmail()).isEmpty();

        return isNewUser ? createUser(user) : updateUser(user);

    }

    private User createUser(User user) {
        Address address = addressResolver.findOrSaveAddress(user);
        User userWithAddressId = user.withAddress(address);
        User savedUser = userService.saveUser(userWithAddressId);
        addressService.addUserToExistAddress(savedUser, address);

        if (Role.LIBRARIAN.equals(user.getRole()) && Objects.nonNull(user.getLibrarians())) {
            return saveUserWithLibrarian(user.getLibrarians(), savedUser);
        }
        return savedUser;
    }

    private User updateUser(User user) {
        Address addressUpdate = addressResolver.updateOrSaveAddress(user);

        User userUpdateAddress = user.withAddress(addressUpdate);
        if (Role.LIBRARIAN.equals(user.getRole()) && Objects.nonNull(user.getLibrarians())) {
            return saveUserWithLibrarian(user.getLibrarians(), userUpdateAddress);
        }
        return userService.saveUser(userUpdateAddress);
    }

    private User saveUserWithLibrarian(Set<Librarian> librarians, User user) {
        Librarian savedLibrarian = librarianService.saveLibrarianForUser(librarians, user);

        return userService.saveUser(user.withLibrarians(Set.of(savedLibrarian)));
    }
}