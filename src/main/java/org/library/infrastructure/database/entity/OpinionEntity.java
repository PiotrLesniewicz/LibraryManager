package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString(of = {"rating", "comment"})
@Builder
@EqualsAndHashCode(of = "opinionId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "opinions")
public class OpinionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opinion_id", nullable = false, unique = true)
    private Integer opinionId;

    @Column(name = "rating", nullable = false)
    private byte rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
