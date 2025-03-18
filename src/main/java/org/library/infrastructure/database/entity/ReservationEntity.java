package org.library.infrastructure.database.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString(of = {"reservationDate", "expiryDate", "cancelledDate", "status"})
@Builder
@EqualsAndHashCode(of = "reservationId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false, unique = true)
    private Integer reservationId;

    @Column(name = "reservation_date", nullable = false)
    private OffsetDateTime reservationDate;

    @Column(name = "expiry_date", nullable = false)
    private OffsetDateTime expiryDate;

    @Column(name = "cancelled_date")
    private OffsetDateTime cancelledDate;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_item_id", nullable = false)
    private BookItemEntity bookItem;
}
