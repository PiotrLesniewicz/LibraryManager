package org.library.infrastructure.database.repository;

import org.library.infrastructure.database.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
    Optional<AddressEntity> findByCityAndStreetAndNumberAndPostCode(String city, String street, String number, String postCode);
}
