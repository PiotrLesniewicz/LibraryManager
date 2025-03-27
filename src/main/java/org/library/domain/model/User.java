package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.library.infrastructure.database.entity.LibrarianEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
public class User {

    Integer userId;
    String userName;
    String name;
    String surname;
    String email;
    String password;
    String phoneNumber;
    LocalDate membershipDate;
    Role role;
    Address address;
    Set<Librarian> librarians;
    Set<Reservation> reservations;
    Set<Loan> loans;

    public Set<Librarian> getLibrarians(){
        if (Objects.isNull(librarians)){
            return new HashSet<>();
        }
        return librarians;
    }
}
