package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString(of = {"name"})
@Builder
@EqualsAndHashCode(of = "publisherId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "publishers")
public class PublisherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id", nullable = false, unique = true)
    private Integer publisherId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "website")
    private String website;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "publishers")
    private Set<BookEntity> books;
}
