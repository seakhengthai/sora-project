package com.demo.gateway.domain.entity;

import com.demo.gateway.domain.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "client_role",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "role_code","client_id"})
        })
public class ClientRole extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="client_id", referencedColumnName="id")
    })
    private Client client;

    @OneToOne
    @JoinColumn(name = "role_code",referencedColumnName = "code")
    private Role role;

    @Enumerated(EnumType.STRING)
    RoleType roleType;
}
