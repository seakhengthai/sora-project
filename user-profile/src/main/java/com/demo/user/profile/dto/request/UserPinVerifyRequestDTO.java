package com.demo.user.profile.dto.request;

import com.demo.user.profile.domain.VerifyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPinVerifyRequestDTO{
    String tpin;
    String tbiometric;
    @JsonProperty("request_key")
    String requestKey;
    @JsonProperty("verify_type")
    private VerifyType verifyType;
    @JsonProperty("additional_data")
    private Map<String, Object> additionalData;
}