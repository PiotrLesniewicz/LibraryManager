package org.library.domain.model;


import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Set;

@With
@Value
@Builder
public class Book {

    Integer bookId;
    String title;
    String isbn;
    Set<Opinion> opinions;
    Set<BookItem> bookItems;
    Set<Author> authors;
    Set<Category> categories;
    Set<Publisher> publishers;
}
