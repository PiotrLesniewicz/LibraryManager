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

    public User downgradeUser() {
        return this.withUserRole(UserRole.USER)
                .withLibrarian(null);
    }

    public User updateFrom(User updated, Address address, Librarian librarian) {
        return this.toBuilder()
                .userName(updated.getUserName() != null
                        ? updated.getUserName()
                        : this.userName)
                .name(updated.getName() != null
                        ? updated.getName()
                        : this.name)
                .surname(updated.getSurname() != null
                        ? updated.getSurname()
                        : this.surname)
                .email(updated.getEmail() != null
                        ? updated.getEmail()
                        : this.email)
                .phoneNumber(updated.getPhoneNumber() != null
                        ? updated.getPhoneNumber()
                        : this.phoneNumber)
                .userRole(librarian != null
                        ? UserRole.LIBRARIAN
                        : this.userRole)
                .address(address != null
                        ? address
                        : this.address)
                .librarian(librarian != null
                        ? librarian
                        : this.librarian)
                .build();
    }
}
