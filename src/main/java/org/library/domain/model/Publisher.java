package org.library.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Set;

@With
@Value
@Builder
public class Publisher {

    Integer publisherId;
    String name;
    String website;
    Set<Book> books;
}
