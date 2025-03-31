package com.demo.gateway.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "client_published_api",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "api_key"})
        })
public class ClientPublishAPI extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable=true,name = "api_key")
    private String apikey;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="client_api_id", referencedColumnName="id")
    })
    private ClientAPI clientAPI;

    @Column(updatable=true, nullable=false,name = "api_code")
    private String apiCode;

    @Column(name = "published_on", updatable=false, nullable=false)
    private Date publishedOn;

    @Column(name = "retired_on", updatable=false, nullable=false)
    private Date retiredOn;

    @Column(name = "num_published", updatable=true, nullable=true)
    private Integer numPublished;

    @Column(name = "is_published", nullable=true)
    private Boolean is_published=false;

    @Column(updatable=false, nullable=false)
    private String version;

}
