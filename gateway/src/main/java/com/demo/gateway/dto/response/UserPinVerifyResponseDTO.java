package com.demo.gateway.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserPinVerifyResponseDTO implements Serializable {
    @JsonProperty("authorize_token")
    private String authorizeToken;
    private boolean valid;
    private String service;

}
