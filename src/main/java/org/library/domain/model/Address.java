package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
public class Address {

    Integer addressId;
    String city;
    String street;
    String number;
    String postCode;
    Set<User> users;

    public Set<User> getUsers(){
        if (Objects.isNull(users)){
            return new HashSet<>();
        }
        return users;
    }
}
