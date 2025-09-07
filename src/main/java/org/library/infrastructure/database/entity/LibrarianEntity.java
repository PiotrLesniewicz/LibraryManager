package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString(of = {"librarianRole"})
@Builder
@EqualsAndHashCode(of = "librarianId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "librarian")
public class LibrarianEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "librarian_id", nullable = false)
    private Integer librarianId;

    @Column(name = "role", nullable = false)
    private String librarianRole;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "librarian")
    private UserEntity user;
}
