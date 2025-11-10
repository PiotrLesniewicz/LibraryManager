package org.library.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.data.DataTestFactory;
import org.library.domain.exception.NotFoundUserException;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Librarian;
import org.library.domain.model.LibrarianRole;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.library.domain.service.UserService;
import org.library.infrastructure.database.entity.UserEntity;
import org.library.infrastructure.database.repository.UserRepository;
import org.library.infrastructure.mapper.UserEntityMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserEntityMapper userEntityMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Clock fixClock;


    @Test
    void shouldCreateUserWithEncodedPasswordAndDefaultRole() {
        // given
        User user = DataTestFactory.defaultUser().withUserRole(null);

        Instant instant = Instant.parse("2023-09-23T00:00:00Z");
        when(fixClock.instant()).thenReturn(instant);
        when(fixClock.getZone()).thenReturn(ZoneId.systemDefault());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // when
        userService.createUser(user, null, null);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userEntityMapper).mapToEntity(captor.capture());

        User capturedUser = captor.getValue();
        assertThat(capturedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(capturedUser.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(capturedUser.getMembershipDate()).isEqualTo(LocalDate.parse("2023-09-23"));
        assertThat(capturedUser.getLibrarian()).isNull();
    }

    @Test
    void shouldCreateLibrarianUser_WhenLibrarianProvided() {
        // given
        User user = DataTestFactory.defaultUser().withUserRole(UserRole.LIBRARIAN);
        Librarian librarian = DataTestFactory.defaultLibrarian();

        Instant instant = Instant.parse("2023-09-23T00:00:00Z");
        when(fixClock.instant()).thenReturn(instant);
        when(fixClock.getZone()).thenReturn(ZoneId.systemDefault());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // when
        userService.createUser(user, librarian, null);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userEntityMapper).mapToEntity(captor.capture());

        User capturedUser = captor.getValue();
        assertThat(capturedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(capturedUser.getUserRole()).isEqualTo(UserRole.LIBRARIAN);
        assertThat(capturedUser.getMembershipDate()).isEqualTo(LocalDate.parse("2023-09-23"));
        assertThat(capturedUser.getLibrarian().getLibrarianRole()).isEqualTo(LibrarianRole.ADMIN);
    }

    @Test
    void shouldUpdateUserDetails_WithoutLibrarian() {
        // given
        User existingUser = DataTestFactory.defaultUser()
                .withUserId(10)
                .withMembershipDate(LocalDate.of(2020, 10, 11));
        User updatedUser = existingUser
                .withUserName("newUserName")
                .withName("NewName")
                .withSurname("NewSurname");

        when(userEntityMapper.mapToEntity(any(User.class))).thenReturn(new UserEntity());
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userEntityMapper.mapFromEntity(any())).thenReturn(updatedUser);

        // when
        User result = userService.updateUser(existingUser, updatedUser, null, null);

        // then
        assertThat(result.getUserId()).isEqualTo(10);
        assertThat(result.getUserName()).isEqualTo("newUserName");
        assertThat(result.getName()).isEqualTo("NewName");
        assertThat(result.getSurname()).isEqualTo("NewSurname");
        assertThat(result.getMembershipDate()).isEqualTo(LocalDate.of(2020, 10, 11));
    }

    @Test
    void shouldPromoteUserToLibrarian() {
        // given
        User existingUser = DataTestFactory.defaultUser()
                .withUserId(1)
                .withUserRole(UserRole.USER);

        Librarian newLibrarian = DataTestFactory.defaultLibrarian().withLibrarianId(10);

        User updatedUser = existingUser.withUserRole(UserRole.LIBRARIAN)
                .withLibrarian(newLibrarian);
        when(userEntityMapper.mapToEntity(any(User.class))).thenReturn(new UserEntity());
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userEntityMapper.mapFromEntity(any())).thenReturn(updatedUser);
        // when
        User result = userService.updateUser(existingUser, updatedUser, null, newLibrarian);

        // then
        assertThat(result.getUserRole()).isEqualTo(UserRole.LIBRARIAN);
        assertThat(result.getLibrarian()).isNotNull();
        assertThat(result.getLibrarian().getLibrarianId()).isEqualTo(10);
    }

    @Test
    void shouldDowngradeLibrarianToUserRole() {
        // given
        User userWithLibrarian = DataTestFactory.librarianUser();

        when(userEntityMapper.mapFromEntity(any()))
                .thenReturn(userWithLibrarian);

        // when
        userService.downgradeLibrarianToUser(userWithLibrarian);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userEntityMapper).mapToEntity(captor.capture());

        User capturedUser = captor.getValue();
        assertThat(capturedUser.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(capturedUser.getLibrarian()).isNull();
    }

    @Test
    void shouldThrowException_WhenUserIsNull() {
        // given, when, then
        assertThatThrownBy(() -> userService.createUser(null, null, null))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("The user must not be empty!");
    }

    @Test
    void shouldThrowException_WhenUserNotFoundById() {
        // given
        Integer userId = 1;
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(NotFoundUserException.class)
                .hasMessage("User with userId: [%s] does not exist".formatted(userId));
    }

    @Test
    void shouldThrowException_WhenCreatingUserWithExistingEmail() {
        // given

        User user = DataTestFactory.defaultUser();
        User existingUser = User.builder().userId(10).email(user.getEmail()).build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new UserEntity()));
        when(userEntityMapper.mapFromEntity(any(UserEntity.class))).thenReturn(existingUser);

        // when, then
        assertThatThrownBy(() -> userService.createUser(user, null, DataTestFactory.defaultAddress()))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("Email: [%s] is already in use by another user.".formatted(user.getEmail()));
    }

    @Test
    void shouldThrowException_WhenUpdatingUserWithExistingEmail(){
        // given
        User user = DataTestFactory.defaultUser().withUserId(10);
        User conflicting = User.builder().email(user.getEmail()).build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new UserEntity()));
        when(userEntityMapper.mapFromEntity(any(UserEntity.class))).thenReturn(conflicting);
        // when, then
        assertThatThrownBy(() -> userService.updateUser(conflicting, user, null, null))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("Email: [%s] is already in use by another user.".formatted(user.getEmail()));
    }
}
