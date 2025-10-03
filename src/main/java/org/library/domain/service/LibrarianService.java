package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.NotFoundLibrarianException;
import org.library.domain.exception.UserValidationException;
import org.library.domain.model.Librarian;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.library.infrastructure.database.entity.LibrarianEntity;
import org.library.infrastructure.database.repository.LibrarianRepository;
import org.library.infrastructure.mapper.LibrarianEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibrarianService {

    private final LibrarianRepository librarianRepository;
    private final LibrarianEntityMapper librarianEntityMapper;
    private final Clock clock;

    @Transactional
    public Librarian createOrUpdateLibrarian(User user) {

        if (!UserRole.LIBRARIAN.equals(user.getUserRole())) {
            return null;
        }

        if (Objects.isNull(user.getLibrarian())) {
            throw new UserValidationException("Librarian data must be provided for user with LIBRARIAN role: [%s]".formatted(user.getUserRole()));
        }

        return findByUserId(user.getUserId())
                .map(existingLibrarian -> getUpdateOrExistingLibrarian(user, existingLibrarian))
                .orElseGet(() -> saveNewLibrarian(user));
    }

    @Transactional
    public void deleteLibrarian(Integer librarianId) {
       librarianRepository.deleteById(librarianId);
    }

    public Librarian saveLibrarian(Librarian librarian) {
        LibrarianEntity toSave = librarianEntityMapper.mapToEntity(librarian);
        LibrarianEntity librarianEntity = librarianRepository.saveAndFlush(toSave);
        return librarianEntityMapper.mapFromEntity(librarianEntity);
    }

    public Librarian findById(Integer librarianId) {
        return librarianRepository.findById(librarianId)
                .map(librarianEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundLibrarianException("Not found librarian for librarianId: [%s]".formatted(librarianId)));
    }


    public Optional<Librarian> findByUserId(Integer userId) {
        return Objects.isNull(userId)
                ? Optional.empty()
                : librarianRepository.findByUserId(userId)
                .map(librarianEntityMapper::mapFromEntity);
    }

    public Librarian findByUserEmail(String email) {
        return librarianRepository.findByUserEmail(email)
                .map(librarianEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundLibrarianException("Not found librarian for email: [%s]".formatted(email)));
    }

    public Long countLibrarian() {
        return librarianRepository.count();
    }

    private Librarian getUpdateOrExistingLibrarian(User user, Librarian librarian) {
        if (!librarian.getLibrarianRole().equals(user.getLibrarian().getLibrarianRole())) {
            librarian = librarian.withLibrarianRole(user.getLibrarian().getLibrarianRole());
            return saveLibrarian(librarian);
        }
        return librarian;
    }

    private Librarian saveNewLibrarian(User user) {
        Librarian librarian = user.getLibrarian()
                .withHireDate(LocalDate.now(clock));
        return saveLibrarian(librarian);
    }
}
