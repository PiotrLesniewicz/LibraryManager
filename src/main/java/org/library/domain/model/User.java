package org.library.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@With
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"address", "reservations", "loans"})
public class User {

    @EqualsAndHashCode.Include
    Integer userId;
    String userName;
    String name;
    String surname;
    String email;
    String password;
    String phoneNumber;
    LocalDate membershipDate;
    UserRole userRole;
    Address address;
    Librarian librarian;
    Set<Reservation> reservations;
    Set<Loan> loans;
    Set<Opinion> opinions;
}
