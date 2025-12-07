package org.library.domain.service;

import lombok.AllArgsConstructor;
import org.library.domain.exception.NotFoundUserException;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.library.infrastructure.database.entity.UserEntity;
import org.library.infrastructure.database.repository.UserRepository;
import org.library.infrastructure.mapper.UserEntityMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor

public class UserService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Transactional
    public User createUser(User user, Librarian librarian, Address address) {
        validateDataUser(user);
        User toSave = user
                .withUserRole(Objects.isNull(librarian) ? UserRole.USER : UserRole.LIBRARIAN)
                .withPassword(passwordEncoder.encode(user.getPassword()))
                .withAddress(address)
                .withLibrarian(librarian)
                .withMembershipDate(LocalDate.now(clock));
        return saveUser(toSave);
    }

    @Transactional
    public void downgradeLibrarianToUser(User user) {
        User toUpdate = user.downgradeUser();
        saveUser(toUpdate);
    }

    @Transactional
    public User updateUser(User existing, User updated, Address address, Librarian librarian) {
        validateDataUser(updated);
        existing = existing.updateFrom(updated, address, librarian);
        return saveUser(existing);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }


    public Optional<User> findUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .map(userEntityMapper::mapFromEntity);
    }

    @Transactional
    public User findByIdWithDetails(Integer userId) {
        return userRepository.findByIdWithDetails(userId)
                .map(userEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundUserException("User with userId: [%s] does not exist".formatted(userId)));
    }

    @Transactional(readOnly = true)
    public Long countUser() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public int findCountUsersForAddress(Integer addressId) {
        return userRepository.findCountUsersForAddress(addressId);
    }

    public User saveUser(User user) {
        UserEntity toSave = userEntityMapper.mapToEntity(user);
        UserEntity saved = userRepository.saveAndFlush(toSave);
        return userEntityMapper.mapFromEntity(saved);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntityMapper::mapFromEntity);
    }

    public User findById(Integer userId) {
        return userRepository.findById(userId)
                .map(userEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundUserException("User with userId: [%s] does not exist".formatted(userId)));
    }

    public User findUserByEmailOrUserName(String input) {
        if (isEmail(input)) {
            return findUserByEmail(input).orElseThrow(
                    () -> new NotFoundUserException("User with email: [%s] does not exist".formatted(input))
            );
        }
        return findUserByUserName(input).orElseThrow(
                () -> new NotFoundUserException("User with userName: [%s] does not exist".formatted(input))
        );
    }

    private void validateDataUser(User user) {
        if (Objects.isNull(user)) {
            throw new UserValidationException("The user must not be empty!");
        }
        validateEmailUniqueness(user);
        validateUserNameUniqueness(user);
    }

    private void validateEmailUniqueness(User user) {
        if (user.getEmail() != null) {
            Optional<User> userWithEmail = findUserByEmail(user.getEmail());
            if (userWithEmail.isPresent() && isDifferentUser(userWithEmail.get(), user)) {
                throw new UserValidationException("Email: [%s] is already in use by another user.".formatted(user.getEmail()));
            }
        }
    }

    private void validateUserNameUniqueness(User user) {
        if (user.getUserName() != null) {
            Optional<User> userWithUserName = findUserByUserName(user.getUserName());
            if (userWithUserName.isPresent() && isDifferentUser(userWithUserName.get(), user)) {
                throw new UserValidationException("UserName: [%s] is already in use by another user.".formatted(user.getUserName()));
            }
        }
    }

    private boolean isDifferentUser(User existingUser, User currentUser) {
        Integer existingUserId = existingUser.getUserId();
        Integer currentUserId = currentUser.getUserId();
        return Objects.isNull(currentUserId) || !Objects.equals(existingUserId, currentUserId);
    }

    private boolean isEmail(String input) {
        return EMAIL_PATTERN.matcher(input).matches();
    }
}
