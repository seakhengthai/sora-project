package com.demo.gateway.logging;

import lombok.Builder;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Collections;
import java.util.List;

@Builder
@ToString
public class HttpRequest {
    final static public String CIF_KEY = "cif";
    final static public String Correlation_ID = "correlation_id";
    private String method;
    private String uri;
    private String path;
    private List cifId;
    private List correlationId;
    private HttpHeaders httpHeaders;
    private String remoteAddress;
    private String body;
    private String scheme;
    private String query;

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public static HttpRequest create(ServerHttpRequest request, String body, boolean isLogHeader) {
        HttpRequest httpRequest = HttpRequest.builder()
                .correlationId(request.getHeaders().getOrDefault(Correlation_ID, Collections.emptyList()))
                .method(request.getMethod().toString())
                .path(request.getPath().toString())
                .uri(request.getURI().toString())
                .cifId(request.getHeaders().getOrDefault(CIF_KEY, Collections.emptyList()))
                .body(body)
                .remoteAddress(request.getRemoteAddress().toString())
                .query(request.getQueryParams().toString())
                .build();
        if (isLogHeader) httpRequest.setHttpHeaders(request.getHeaders());
        return httpRequest;
    }
}
