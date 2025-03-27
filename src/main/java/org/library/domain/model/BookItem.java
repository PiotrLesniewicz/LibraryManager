package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Set;

@With
@Value
@Builder
public class BookItem {

    Integer bookItemId;
    String barcode;
    short yearOfPublication;
    Book book;
    Set<Loan> loans;
    Set<Reservation> reservations;
}
