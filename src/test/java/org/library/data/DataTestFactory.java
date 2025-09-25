package org.library.data;

import org.library.domain.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;

@Component
public class DataTestFactory {

    public static User defaultUser() {
        return User.builder()
                .userName("defaultUserName")
                .name("defaultName")
                .surname("defaultSurname")
                .email("default@email.com")
                .password("default")
                .userRole(UserRole.USER)
                .build();
    }

    public static User differentUser() {
        return User.builder()
                .userName("differentUserName")
                .name("differentName")
                .surname("differentSurname")
                .email("different@email.com")
                .password("different")
                .userRole(UserRole.USER)
                .build();
    }

    public static User userForNewAccount() {
        return User.builder()
                .userName("newUser")
                .name("John")
                .surname("Smith")
                .email("john.smith@example.com")
                .password("securePass")
                .userRole(UserRole.USER)
                .address(defaultAddress())
                .build();
    }

    public static User userWithExistingAddress(Address existingAddress) {
        return User.builder()
                .userName("existingAddressUser")
                .name("Anna")
                .surname("Taylor")
                .email("anna.taylor@example.com")
                .password("password123")
                .userRole(UserRole.USER)
                .address(existingAddress)
                .build();
    }

    public static User librarianUser() {
        return User.builder()
                .userName("librarianUser")
                .name("Frank")
                .surname("Nelson")
                .email("frank.nelson@example.com")
                .password("librarianPass")
                .userRole(UserRole.LIBRARIAN)
                .librarian(Librarian.builder()
                        .librarianRole(LibrarianRole.ADMIN)
                        .build())
                .address(defaultAddress())
                .build();
    }

    public static Address defaultAddress() {
        return Address.builder()
                .city("DefaultCity")
                .street("Main Street")
                .number("1A")
                .postCode("00-001")
                .users(new HashSet<>())
                .build();
    }

    public static Address differentAddress() {
        return Address.builder()
                .city("NewCity")
                .street("NewStreet")
                .number("105/22")
                .postCode("26-260")
                .users(new HashSet<>())
                .build();
    }

    public static Librarian librarianExistingInDB() {
        return Librarian.builder()
                .librarianId(2)
                .librarianRole(LibrarianRole.TECHNIC)
                .hireDate(LocalDate.of(2021, 5, 15))
                .build();
    }
}

