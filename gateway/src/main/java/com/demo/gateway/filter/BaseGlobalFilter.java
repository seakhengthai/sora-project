package com.demo.gateway.filter;

import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.service.AuthenticateManager;
import com.demo.gateway.service.BaseClientService;
import com.demo.gateway.utils.HttpUtilsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class BaseGlobalFilter extends BaseClientService {

    @Value("${ignoreResources:**/actuator/health}")
    protected String ignoreResources;

    @Value("${token.endpoint:**/oauth/token/**}")
    protected String tokenEndpoint;

    private final AuthenticateManager authenticateManager;

    BaseGlobalFilter(AuthenticateManager authenticateManager) {
        this.authenticateManager = authenticateManager;
    }

    protected Client getClient(ServerWebExchange exchange) {
        return authenticateManager.
                create(exchange.getRequest().getHeaders())
                .getClient();
    }

    boolean skipCheckUrl(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        List<String> ignoreResourcesList = Arrays.asList(ignoreResources.split(","));
        Optional<String> foundIgnoreResources = ignoreResourcesList.stream()
                .filter(resource -> HttpUtilsRequest.isIgnoreResources(resource, path)).
                        findFirst();
        if (foundIgnoreResources.isPresent()) {
            return true;
        }
        return false;
    }

    protected boolean isAuthTokenEndpoint(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        List<String> ignoreResourcesList = Arrays.asList(tokenEndpoint.split(","));
        Optional<String> foundIgnoreResources = ignoreResourcesList.stream()
                .filter(resource -> HttpUtilsRequest.isIgnoreResources(resource, path)).
                        findFirst();
        return foundIgnoreResources.isPresent();
    }

}
