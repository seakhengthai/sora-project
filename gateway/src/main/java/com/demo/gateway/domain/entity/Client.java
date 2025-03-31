package com.demo.gateway.domain.entity;


import com.demo.gateway.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
@Slf4j
public class Client extends BaseEntity {

    @Id
    private String id;
    private String name;
    private String secret;
    private String roles;
    private Long accessTokenExpired;
    private Long refreshTokenExpired;
    private String channel;
    private String grantType;
    private String scopes;
    private Boolean isEnabledHash = true;

    @OneToOne(mappedBy = "client", fetch = FetchType.EAGER)
    @JsonIgnore
    private ClientAPI clientAPI;

    @JsonIgnore
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientRole> clientRoles;

    @JsonIgnore
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientPolicy> clientPolicies;


    public List<String> getGrantTypes() {
        return Arrays.asList(this.getGrantType().split("\\s*,\\s*"));
    }


    public List<String> buildScopes() {
        return Arrays.asList(this.getScopes().split("\\s*,\\s*"));
    }

    public Optional<ClientPolicy> isPolicyExisted(String policyName) {
        Optional<ClientPolicy> first = this.getClientPolicies()
                .stream()
                .filter(clientPolicy ->
                        Objects.nonNull(clientPolicy.getPolicy()) &&
                                clientPolicy.getStatus().equals(Status.ACTIVE) &&
                                StringUtils.hasLength(clientPolicy.getPolicy().getName())
                                && clientPolicy.getPolicy().getName().equalsIgnoreCase(policyName))
                .findAny();
        return first;
    }

}
