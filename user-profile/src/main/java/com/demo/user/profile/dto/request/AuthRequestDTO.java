package com.demo.user.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO extends AuthBaseRequestDTO {

    @JsonProperty("is_relogin")
    private boolean isRelogin;

    @JsonProperty("biometric_token")
    private String biometricToken;

    @JsonProperty("is_disable_biometric")
    private boolean isDisableBio;
}
