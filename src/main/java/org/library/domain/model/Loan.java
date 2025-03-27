package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.OffsetDateTime;
import java.util.Set;

@With
@Value
@Builder
public class Loan {

    Integer loanId;
    OffsetDateTime loanDate;
    OffsetDateTime returnDate;
    String status;
    Set<LoanPenalty> loanPenalties;
    User user;
    BookItem bookItem;

}
