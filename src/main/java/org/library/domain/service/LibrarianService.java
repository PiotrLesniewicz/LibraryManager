package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.LibrarianMissingException;
import org.library.domain.model.Librarian;
import org.library.domain.model.User;
import org.library.infrastructure.database.entity.LibrarianEntity;
import org.library.infrastructure.database.repository.LibraryRepository;
import org.library.mapper.LibrarianEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LibrarianService {

    private final LibraryRepository libraryRepository;
    private final LibrarianEntityMapper librarianEntityMapper;

    @Transactional
    public Librarian saveLibrarian(Librarian librarian) {
        LibrarianEntity toSave = librarianEntityMapper.mapToEntity(librarian);
        LibrarianEntity librarianEntity = libraryRepository.saveAndFlush(toSave);
        return librarianEntityMapper.mapFromEntity(librarianEntity);
    }

    public Optional<Librarian> findById(Integer librarianId) {
        return libraryRepository.findById(librarianId)
                .map(librarianEntityMapper::mapFromEntity);
    }

    public Long countLibrarian() {
        return libraryRepository.count();
    }

    @Transactional
    public Librarian saveLibrarianForUser(Set<Librarian> librarians, User user) {
        if (librarians.size() != 1) {
            throw new LibrarianMissingException("The user must have exactly one librarian. Current size of librarians: [%s]."
                    .formatted(librarians.size()));
        }
        Librarian librarian = librarians.stream()
                .findFirst()
                .orElseThrow(() -> new LibrarianMissingException("No librarian found for the user with email: [%s]"
                        .formatted(user.getEmail())));

        return saveLibrarian(librarian.withUser(user));
    }
}
