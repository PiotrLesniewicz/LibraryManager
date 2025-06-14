package org.library.domain.service;

import lombok.RequiredArgsConstructor;
import org.library.domain.exception.NotFoundLibrarianException;
import org.library.domain.model.Librarian;
import org.library.infrastructure.database.entity.LibrarianEntity;
import org.library.infrastructure.database.repository.LibrarianRepository;
import org.library.mapper.LibrarianEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LibrarianService {

    private final LibrarianRepository librarianRepository;
    private final LibrarianEntityMapper librarianEntityMapper;

    @Transactional
    public Librarian saveLibrarian(Librarian librarian) {
        LibrarianEntity toSave = librarianEntityMapper.mapToEntity(librarian);
        LibrarianEntity librarianEntity = librarianRepository.saveAndFlush(toSave);
        return librarianEntityMapper.mapFromEntity(librarianEntity);
    }

    @Transactional
    public void deleteLibrarian(Integer librarianId) {
        findById(librarianId);
        librarianRepository.deleteById(librarianId);
    }

    @Transactional(readOnly = true)
    public Librarian findById(Integer librarianId) {
        return librarianRepository.findById(librarianId)
                .map(librarianEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundLibrarianException("Not found librarian for librarianId: [%s]".formatted(librarianId)));
    }

    @Transactional(readOnly = true)
    public Librarian findByUserId(Integer userId) {
        return librarianRepository.findByUserId(userId)
                .map(librarianEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundLibrarianException("Not found librarian for userId: [%s]".formatted(userId)));
    }

    @Transactional(readOnly = true)
    public Librarian findByUserEmail(String email) {
        return librarianRepository.findByUserEmail(email)
                .map(librarianEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundLibrarianException("Not found librarian for email: [%s]".formatted(email)));
    }

    @Transactional(readOnly = true)
    public Long countLibrarian() {
        return librarianRepository.count();
    }
}
