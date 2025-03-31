package com.demo.gateway.filter;

import com.demo.gateway.constants.FilterOrder;
import com.demo.gateway.constants.PolicyConstants;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.service.AuthenticateManager;
import com.demo.gateway.service.ClientAPIAuthorizeAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component("APIKEYPolicy")
@Slf4j
@RefreshScope
public class APIAuthorizationFilter
        extends BaseGlobalFilter
        implements org.springframework.cloud.gateway.filter.GlobalFilter,
        Ordered {

    private final ClientAPIAuthorizeAPI clientAPIAuthorizeAPI;

    APIAuthorizationFilter(AuthenticateManager authenticateManager, ClientAPIAuthorizeAPI clientAPIAuthorizeAPI) {
        super(authenticateManager);
        this.clientAPIAuthorizeAPI = clientAPIAuthorizeAPI;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("APIAuthorizationFilter filter is filtering.");
        if (super.skipCheckUrl(exchange, chain))
            return chain.filter(exchange);
        Client client = super.getClient(exchange);
        if (client.isPolicyExisted(PolicyConstants.APIKEY_POLICY).isEmpty()) {
            return chain.filter(exchange);
        }
        clientAPIAuthorizeAPI.validateMatchingAPI(client, exchange);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.API_AUTHORIZATION_ORDER;
    }
}
