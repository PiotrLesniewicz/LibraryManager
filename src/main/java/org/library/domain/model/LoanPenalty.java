package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@With
@Value
@Builder
public class LoanPenalty {

    Integer loanPenaltyId;
    BigDecimal penaltyAmount;
    OffsetDateTime penaltyDate;
    String penaltyType;
    Loan loan;
}
