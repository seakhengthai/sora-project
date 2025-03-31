package com.demo.gateway.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_policy")
public class ClientPolicy extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="client_id", referencedColumnName="id")
    })
    @JoinColumn()
    private Client client;

    @JoinColumn(name = "policy_code",referencedColumnName = "code")
    @OneToOne
    private Policy policy;

    private String policyConfig;

}
