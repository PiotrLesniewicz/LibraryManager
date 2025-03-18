package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString(of = {"barcode"})
@Builder
@EqualsAndHashCode(of = "bookItemId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_item")
public class BookItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_item_id", nullable = false, unique = true)
    private Integer bookItemId;

    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    @Column(name = "year_of_publication", nullable = false)
    private short yearOfPublication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookItem")
    private Set<LoanEntity> loans;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookItem")
    private Set<ReservationEntity> reservations;
}
