package org.library.domain.model;


import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.OffsetDateTime;

@With
@Value
@Builder
public class Reservation {

    Integer reservationId;
    OffsetDateTime reservationDate;
    OffsetDateTime expiryDate;
    OffsetDateTime cancelledDate;
    String status;
    User user;
    BookItem bookItem;
}
