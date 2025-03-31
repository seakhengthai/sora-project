package com.demo.gateway.domain.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "client_token_logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientTokenLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "client_id")
    String clientId;

    @Column(name = "cif")
    private String cif;

    @Column(name = "device_id")
    String deviceId;

}
