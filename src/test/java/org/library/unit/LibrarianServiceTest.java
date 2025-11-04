package org.library.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.data.DataTestFactory;
import org.library.domain.exception.NotFoundLibrarianException;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Librarian;
import org.library.domain.model.LibrarianRole;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.library.domain.service.LibrarianService;
import org.library.infrastructure.database.entity.LibrarianEntity;
import org.library.infrastructure.database.repository.LibrarianRepository;
import org.library.infrastructure.mapper.LibrarianEntityMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibrarianServiceTest {

    @InjectMocks
    private LibrarianService librarianService;
    @Mock
    private LibrarianRepository librarianRepository;
    @Mock
    private LibrarianEntityMapper librarianEntityMapper;
    @Mock
    private Clock fixClock;

    @Test
    void shouldThrowException_WhenNotFoundLibrarianByLibrarianId() {
        // given
        int librarianId = 10;
        Mockito.when(librarianRepository.findById(librarianId))
                .thenReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> librarianService.findById(librarianId))
                .isInstanceOf(NotFoundLibrarianException.class)
                .hasMessage("Not found librarian for librarianId: [%s]".formatted(librarianId));
    }

    @Test
    void shouldReturnEmptyOptional_WhenUserIdIsNull() {
        // given, when
        Optional<Librarian> result = librarianService.findByUserId(null);

        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNullLibrarian_WhenUserHasUserRole() {
        // given
        User user = DataTestFactory.defaultUser();

        // when
        Librarian result = librarianService.createOrUpdateLibrarian(user);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldThrowException_WhenUserHasLibrarianRoleAndLibrarianIsNull() {
        // given
        User user = DataTestFactory.defaultUser()
                .withUserRole(UserRole.LIBRARIAN)
                .withLibrarian(null);

        // when, then
        assertThatThrownBy(() -> librarianService.createOrUpdateLibrarian(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Librarian data must be provided for user with LIBRARIAN role: [%s]".formatted(user.getUserRole()));
    }

    @Test
    void shouldReturnExistingLibrarian_WhenUserIdNonNullAndLibrarianExistingInDB() {
        // given
        Librarian expected = DataTestFactory.librarianExistingInDB();

        User user = DataTestFactory.defaultUser()
                .withUserId(10)
                .withUserRole(UserRole.LIBRARIAN)
                .withLibrarian(expected);

        when(librarianRepository.findByUserId(user.getUserId()))
                .thenReturn(Optional.of(new LibrarianEntity()));
        when(librarianEntityMapper.mapFromEntity(any(LibrarianEntity.class)))
                .thenReturn(expected);

        // when
        Librarian result = librarianService.createOrUpdateLibrarian(user);

        // then
        verify(librarianRepository).findByUserId(user.getUserId());
        verify(librarianRepository, never()).saveAndFlush(any());
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldCorrectlyUpdateRoleExistingLibrarian_WhenUserIdNonNullAndLibrarianExistingInDB() {
        // given
        Librarian librarianInDB = DataTestFactory.librarianExistingInDB()
                .withLibrarianRole(LibrarianRole.ASSISTANT);

        User user = DataTestFactory.defaultUser()
                .withUserId(10)
                .withUserRole(UserRole.LIBRARIAN)
                .withLibrarian(librarianInDB.withLibrarianRole(LibrarianRole.ADMIN));

        Librarian expected = librarianInDB.withLibrarianRole(LibrarianRole.ADMIN);

        when(librarianRepository.findByUserId(user.getUserId()))
                .thenReturn(Optional.of(new LibrarianEntity()));
        when(librarianEntityMapper.mapFromEntity(any(LibrarianEntity.class)))
                .thenReturn(librarianInDB)
                .thenReturn(expected);
        when(librarianEntityMapper.mapToEntity(any(Librarian.class)))
                .thenReturn(new LibrarianEntity());
        when(librarianRepository.saveAndFlush(any(LibrarianEntity.class)))
                .thenReturn(new LibrarianEntity());

        // when
        Librarian result = librarianService.createOrUpdateLibrarian(user);

        // then
        verify(librarianRepository).findByUserId(user.getUserId());
        verify(librarianRepository).saveAndFlush(any(LibrarianEntity.class));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldCorrectlyCreateLibrarian_WhenLibrarianNonExistingInDB() {
        // given
        User user = DataTestFactory.librarianUser();
        Instant instant = Instant.parse("2023-09-23T00:00:00Z");
        ZoneId zoneId = ZoneId.systemDefault();

        when(fixClock.instant()).thenReturn(instant);
        when(fixClock.getZone()).thenReturn(zoneId);

        Librarian expected = user.getLibrarian()
                .withLibrarianId(3)
                .withHireDate(LocalDate.now(fixClock));

        when(librarianEntityMapper.mapFromEntity(any(LibrarianEntity.class))).thenReturn(expected);
        when(librarianEntityMapper.mapToEntity(any(Librarian.class))).thenReturn(new LibrarianEntity());
        when(librarianRepository.saveAndFlush(any(LibrarianEntity.class))).thenReturn(new LibrarianEntity());

        // when
        Librarian result = librarianService.createOrUpdateLibrarian(user);

        // them
        verify(librarianRepository).saveAndFlush(any(LibrarianEntity.class));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldDeleteLibrarian_WhenLibrarianExists() {
        // given
        int librarianId = 5;
        doNothing().when(librarianRepository).deleteById(librarianId);

        // when
        librarianService.deleteLibrarian(librarianId);

        // then
        verify(librarianRepository).deleteById(librarianId);
    }
}