package com.demo.gateway.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpMethod;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "apis_def",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"code"}),
                @UniqueConstraint(columnNames = {"service", "method", "path", "version"})
        })
public class API extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "service")
    String service;

    @Column(name = "path")
    String path;

    @Column(name = "method")
    private String method;

    @Column(name = "version")
    String version;

}
