package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString(of = {"userName", "name", "surname", "email"})
@Builder
@EqualsAndHashCode(of = "userId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class
UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "membership_date", nullable = false)
    private LocalDate membershipDate;

    @Column(name = "role", nullable = false)
    private String userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "librarian_id", unique = true)
    private LibrarianEntity librarian;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<ReservationEntity> reservations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<LoanEntity> loans;

    // todo tymczasowo CascadeType.REMOVE, do refaktoru – docelowo ręczne zarządzanie w serwisie.
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<OpinionEntity> opinions;
}
