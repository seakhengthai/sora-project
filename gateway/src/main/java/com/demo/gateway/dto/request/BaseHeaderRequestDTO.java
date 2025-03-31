package com.demo.gateway.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseHeaderRequestDTO {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("x-api-key")
    private String xApiKey;

    @JsonProperty("so")
    private String os;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty ("app_version")
    String appVersion;

    @JsonProperty ("location")
    String location;

    @JsonProperty ("language")
    String language;
}
