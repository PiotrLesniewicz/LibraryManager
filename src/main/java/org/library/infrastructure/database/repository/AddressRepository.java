package org.library.infrastructure.database.repository;

import org.library.infrastructure.database.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
    Optional<AddressEntity> findByCityAndStreetAndNumberAndPostCode(String city, String street, String number, String postCode);

    @Query("""
            SELECT ads FROM AddressEntity ads
            JOIN ads.users u
            WHERE u.userId = :userId""")
    Optional<AddressEntity> findByUserId(@Param("userId") Integer userId);
}
