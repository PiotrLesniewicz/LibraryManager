package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Set;

@With
@Value
@Builder
public class Category {

    Integer categoryId;
    String name;
    Set<Book> books;
}
