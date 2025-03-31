package com.demo.gateway.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "role",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "code"})
        })
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code",nullable = false)
    String code;

    @Column(name = "name")
    String name;

}
