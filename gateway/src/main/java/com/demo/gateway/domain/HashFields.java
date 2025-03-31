package com.demo.gateway.domain;

import com.demo.gateway.constants.APIRequestHeaderConstants;
import com.demo.gateway.utils.HttpHeaderUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

@Builder
@Getter
@ToString
public class HashFields {

    String deviceId;
    String requestUrl;
    String requestMethod;
    String clientId;
    String requestTime;
    String statusCode;
    String tpin;
    String appVersion;

    public void setStatusCode(String statusCode){
        this.statusCode =statusCode;
    }
    public static HashFields fromExchange(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return HashFields
                .builder()
                .deviceId(deviceId(headers))
                .clientId(clientId(headers))
                .requestUrl(exchange.getRequest().getPath().value())
                .statusCode(exchange.getResponse().getStatusCode().value() + "")
                .requestMethod(exchange.getRequest().getMethod().name())
                .requestTime(HttpHeaderUtils.getClientRequestTime(headers))
                .tpin(HttpHeaderUtils.tpin(headers))
                .appVersion(HttpHeaderUtils.appVersion(headers))
                .build();
    }

    public static String clientId(HttpHeaders headers) {
        return headers.containsKey(APIRequestHeaderConstants.CLIENT_ID)
                ? headers.get(APIRequestHeaderConstants.CLIENT_ID).get(0) : "";
    }

    public static String deviceId(HttpHeaders headers) {
        return headers.containsKey(APIRequestHeaderConstants.DEVICE_ID)
                ? headers.get(APIRequestHeaderConstants.DEVICE_ID).get(0) : "";
    }
}
