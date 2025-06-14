package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.LocalDate;

@With
@Value
@Builder
public class Librarian {

    Integer librarianId;
    LibrarianRole librarianRole;
    LocalDate hireDate;
    User user;
}
