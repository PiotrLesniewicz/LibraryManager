package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountUserService {

    private final UserService userService;
    private final AddressManager addressManager;
    private final LibrarianService librarianService;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

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

    @Transactional
    public void deleteAccountUser(User user) {
        addressManager.dissociateUserFromCurrentAddress(user);
        Integer userId = user.getUserId();
        if (UserRole.LIBRARIAN.equals(user.getUserRole())) {
            Optional<Librarian> librarian = librarianService.findByUserId(userId);
            librarian.ifPresent(value -> librarianService.deleteLibrarian(value.getLibrarianId()));
        }
        userService.deleteUser(userId);
    }

    @Transactional
    public void removeLibrarian(User user) {
        if (Objects.nonNull(user.getLibrarian())) {
            librarianService.deleteLibrarian(user.getLibrarian().getLibrarianId());
            userService.saveUser(
                    user.withUserRole(UserRole.USER)
                            .withLibrarian(null)
            );
        }
    }

    private User createUser(User user) {
        Address address = addressManager.findOrCreateNewAddress(user);
        User toSave = getPasswordEncode(user)
                .withMembershipDate(LocalDate.now(clock));
        return saveUser(toSave, address);
    }

    private User updateUser(User user) {
        Address addressUpdate = addressManager.updateOrCreateNewAddress(user);
        return saveUser(user, addressUpdate);
    }

    private User getPasswordEncode(User user) {
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        return user.withPassword(encode);
    }

    private User saveUser(User user, Address address) {
        Librarian librarian = getOrCreateLibrarian(user);
        User toSave = user.withAddress(address).withLibrarian(librarian);
        User savedUser = userService.saveUser(toSave);
        addressManager.addUserToAddress(savedUser, address);
        return savedUser;
    }

    private Librarian getOrCreateLibrarian(User user) {
        if (!UserRole.LIBRARIAN.equals(user.getUserRole())) {
            return null;
        }

        if (user.getLibrarian() == null) {
            throw new UserValidationException("Librarian data must be provided for user with LIBRARIAN role");
        }

        if (user.getUserId() != null) {
            Optional<Librarian> existing = librarianService.findByUserId(user.getUserId());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        Librarian librarian = user.getLibrarian()
                .withHireDate(LocalDate.now(clock));
        return librarianService.saveLibrarian(librarian);
    }
}