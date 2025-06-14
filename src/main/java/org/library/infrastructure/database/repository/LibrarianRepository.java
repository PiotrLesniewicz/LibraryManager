package org.library.infrastructure.database.repository;

import org.library.infrastructure.database.entity.LibrarianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LibrarianRepository extends JpaRepository<LibrarianEntity, Integer> {

    @Query("""
            SELECT lib FROM LibrarianEntity lib
            JOIN lib.user u
            WHERE u.userId = :userId""")
    Optional<LibrarianEntity> findByUserId(@Param("userId") Integer userId);

    @Query("""
            SELECT lib FROM LibrarianEntity lib
            JOIN lib.user u
            WHERE u.email = :email""")
    Optional<LibrarianEntity> findByUserEmail(@Param("email") String email);
}
