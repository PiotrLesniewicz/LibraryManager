package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString(of = {"penaltyAmount", "penaltyDate", "penaltyType"})
@Builder
@EqualsAndHashCode(of = "loanPenaltyId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan_penalty")
public class LoanPenaltyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_penalty_id", nullable = false, unique = true)
    private Integer loanPenaltyId;

    @Column(name = "penalty_amount", nullable = false)
    private BigDecimal penaltyAmount;

    @Column(name = "penalty_date", nullable = false)
    private OffsetDateTime penaltyDate;

    @Column(name = "penalty_type", nullable = false)
    private String penaltyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;
}
