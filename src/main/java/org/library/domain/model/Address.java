package org.library.domain.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@With
@Value
@Builder(toBuilder = true)
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
        if (users == null) {
            return new HashSet<>();
        }
        return users;
    }

    public Address withAddedUser(User user) {
        Set<User> update = new HashSet<>(this.getUsers());
        update.add(user);
        return withUsers(update);
    }

    public Address withRemovedUser(User user) {
        Set<User> update = new HashSet<>(this.getUsers());
        update.remove(user);
        return withUsers(update);
    }

    public boolean isAddressChanged(Address update) {
        return !this.getCity().equals(update.getCity())
                || !this.getStreet().equals(update.getStreet())
                || !this.getNumber().equals(update.getNumber())
                || !this.getPostCode().equals(update.getPostCode());
    }

    public Address updateFrom(Address updated) {
        return this.toBuilder()
                .city(updated.getCity() != null
                        ? updated.getCity()
                        : this.city)
                .street(updated.getStreet() != null
                        ? updated.getStreet()
                        : this.street)
                .number(updated.getNumber() != null
                        ? updated.getNumber()
                        : this.number)
                .postCode(updated.getPostCode() != null
                        ? updated.getPostCode()
                        : this.postCode)
                .build();
    }
}
