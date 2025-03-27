package org.library.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.With;

import java.time.LocalDate;

@Getter
@With
@Value
@Builder
public class Opinion {

    Integer opinionId;
    byte rating;
    String comment;
    LocalDate createDate;
    Book book;
    User user;
}
