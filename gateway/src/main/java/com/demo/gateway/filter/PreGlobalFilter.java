package com.demo.gateway.filter;


import com.demo.gateway.constants.FilterOrder;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import com.demo.gateway.service.AuthenticateManager;
import com.demo.gateway.utils.APIUtilsPattern;
import com.demo.gateway.utils.HttpHeaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Component
@Slf4j
@RefreshScope
public class PreGlobalFilter
        extends BaseGlobalFilter
        implements org.springframework.cloud.gateway.filter.GlobalFilter,
        Ordered {

    @Value("${ignoreResources:**/actuator/health}")
    private String ignoreResources;

    PreGlobalFilter(AuthenticateManager authenticateManager) {
        super(authenticateManager);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("Global filter is filtering.");
        if (super.skipCheckUrl(exchange, chain))
            return chain.filter(exchange);
        Client client =
                super.getClient(exchange);
        this.validateClientDetails(client, exchange);
        this.filterClientScopes(exchange, client);
        exchange.getRequest()
                .mutate()
                .header("start_time", System.currentTimeMillis() + "")
                .header("channel", client.getChannel());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.GLOBAL_CLIENT_FILTER;
    }

    private void filterClientScopes(ServerWebExchange exchange,
                                    Client client) {
        var scopes =
                client.buildScopes();
        String backendContext = APIUtilsPattern
                .getServiceAndDownStreamUrl(exchange.getRequest().getPath()
                        .value())._1;
        Optional<String> first = scopes
                .stream()
                .filter(sc -> sc.equals(backendContext))
                .findFirst();
        if (first.isEmpty())
            throw new AppException(AppErrorCode.AUTH_INVALID_SCOPE);
    }

    private void validateClientDetails(Client client, ServerWebExchange exchange) {
        this.validateClientSecret(client, HttpHeaderUtils.clientSecret(exchange.getRequest().getHeaders()));
    }

}
