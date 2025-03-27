package org.library.infrastructure.database.repository;

import org.library.infrastructure.database.entity.LibrarianEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<LibrarianEntity, Integer> {
}
