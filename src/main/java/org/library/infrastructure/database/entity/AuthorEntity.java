package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString(of = {"name", "surname"})
@Builder
@EqualsAndHashCode(of = "authorId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false, unique = true)
    private Integer authorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "biography")
    private String biography;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    private Set<BookEntity> books;
}
