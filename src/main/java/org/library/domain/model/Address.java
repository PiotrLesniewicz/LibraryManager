package org.library.domain.model;

import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "users")
public class Address {

    @EqualsAndHashCode.Include
    Integer addressId;
    String city;
    String street;
    String number;
    String postCode;
    Set<User> users;

    public Set<User> getUsers() {
        if (Objects.isNull(users)) {
            return new HashSet<>();
        }
        return users;
    }
}
