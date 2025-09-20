package org.library.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.domain.exception.NotFoundLibrarianException;
import org.library.domain.service.LibrarianService;
import org.library.infrastructure.database.repository.LibrarianRepository;
import org.library.infrastructure.mapper.LibrarianEntityMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LibrarianServiceTest {

    @InjectMocks
    private LibrarianService librarianService;
    @Mock
    private LibrarianRepository librarianRepository;
    @Mock
    private LibrarianEntityMapper librarianEntityMapper;


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
}