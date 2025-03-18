package org.library.infrastructure.database.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString(of = {"title"})
@Builder
@EqualsAndHashCode(of = "bookId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false, unique = true)
    private Integer bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private Set<OpinionEntity> opinions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private Set<BookItemEntity> bookItems;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "author_id", nullable = false)

    )
    private Set<AuthorEntity> authors;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false)

    )
    private Set<CategoryEntity> categories;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "books_publishers",
            joinColumns = @JoinColumn(name = "book_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "publisher_id", nullable = false)

    )
    private Set<PublisherEntity> publishers;
}
