package com.demo.gateway.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AuthRequestDTO extends BaseHeaderRequestDTO {

    @NotNull(message = "Grant type must be not null.")
    @JsonProperty("grant_type")
    private String grantType;
    private String username;
    private String password;
    @JsonProperty("is_relogin")
    private boolean isReLogin;
    @JsonProperty("refresh_token")
    private String refreshToken;

    private String deviceInfo;
}
