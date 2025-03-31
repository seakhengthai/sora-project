package com.demo.gateway.logging;

import com.demo.gateway.domain.LogType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class ApiLog {

    private int status;
    @JsonProperty("request_ip")
    private String requestIp;

    @JsonProperty("user_id")
    private String userId;

    private String channel;
    private String timestamp;
    private Long duration;

    @JsonProperty("client_id")
    private String clientId;

    private String service;

    @JsonProperty("device_id")
    private String deviceId;
    @JsonProperty("request_method")
    private String requestMethod;

    @JsonProperty("request_uri")
    private String requestUri;

    @JsonProperty("correction_id")
    private String correctionId;
    @JsonProperty("api_code")
    private String apiCode;

    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty("route_id")
    private String routeId;

    private LogType type;

    private Map<String,Object> body;

    @JsonProperty("is_success_ful")
    private boolean isSuccessful = true;

}
