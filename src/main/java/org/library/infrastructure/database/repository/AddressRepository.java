package org.library.infrastructure.database.repository;

import org.library.infrastructure.database.entity.AddressEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
    Optional<AddressEntity> findByCityAndStreetAndNumberAndPostCode(String city, String street, String number, String postCode);

    @NonNull
    @EntityGraph(attributePaths = {"users"})
    Optional<AddressEntity> findById(@NonNull Integer addressId);
}
