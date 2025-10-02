package org.library.infrastructure.database.repository;

import org.library.infrastructure.database.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @EntityGraph(attributePaths = {"address", "librarian", "address.users"})
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUserName(String userName);

    @Query("""
            SELECT COUNT(u) FROM UserEntity u
            WHERE u.address.addressId = :addressId""")
    int findCountUsersForAddress(@Param("addressId") Integer addressId);
}
