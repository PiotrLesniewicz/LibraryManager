package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Set;

@With
@Value
@Builder
public class Author {

    Integer authorId;
    String name;
    String surname;
    String biography;
    Set<Book> books;
}
