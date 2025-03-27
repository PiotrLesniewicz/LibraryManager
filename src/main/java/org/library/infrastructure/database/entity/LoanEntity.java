package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@ToString(of = {"loanDate", "returnDate", "status"})
@Builder
@EqualsAndHashCode(of = "loanId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id", nullable = false, unique = true)
    private Integer loanId;

    @Column(name = "loan_date", nullable = false)
    private OffsetDateTime loanDate;

    @Column(name = "return_date", nullable = false)
    private OffsetDateTime returnDate;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "loan")
    private Set<LoanPenaltyEntity> loanPenalties;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_item_id", nullable = false)
    private BookItemEntity bookItem;

}
