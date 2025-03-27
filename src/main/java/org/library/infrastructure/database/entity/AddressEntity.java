package org.library.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString(of = {"city", "street", "number", "postCode"})
@Builder
@EqualsAndHashCode(of = "addressId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false, unique = true)
    private Integer addressId;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "post_code", nullable = false)
    private String postCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
    private Set<UserEntity> users;
}
