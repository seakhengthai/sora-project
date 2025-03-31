package com.demo.gateway.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Table(name = "client_apis",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "client_id" }),
                @UniqueConstraint(columnNames = { "api_key" })})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClientAPI extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="client_id", referencedColumnName="id"),
    })
    @JsonIgnore
    private Client client;

    @OneToMany(mappedBy = "clientAPI", fetch = FetchType.EAGER)
    private Set<ClientPublishAPI> clientPublishAPIS;

    @Column(updatable=true, nullable=false,name = "api_key")
    private String apikey;

}
