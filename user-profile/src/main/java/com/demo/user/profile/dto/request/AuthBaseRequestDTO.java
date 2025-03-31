package com.demo.user.profile.dto.request;

import com.demo.user.profile.domain.GrantType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuthBaseRequestDTO {
    private String cif;
    private String password;
    @NotNull(message = "username must be not null.")
    @JsonProperty("username")
    private String userName;
    @NotNull(message = "Grant type must be not null.")
    @JsonProperty("grant_type")
    private GrantType grantType;
}
