package com.demo.gateway.filter;

import com.demo.gateway.constants.FilterOrder;
import com.demo.gateway.constants.PolicyConstants;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.domain.entity.ClientPolicy;
import com.demo.gateway.service.AuthenticateManager;
import com.demo.gateway.service.ClientService;
import com.demo.gateway.utils.HttpHeaderUtils;
import com.demo.gateway.utils.TokenUtils;
import com.demo.gateway.validator.TokenValidator;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.demo.gateway.utils.HttpHeaderUtils.clientId;
import static com.demo.gateway.utils.TokenUtils.CIF;


@Component("oauth2_token")
@Slf4j
public class OAuthTokenFilter
        extends BaseGlobalFilter
        implements GlobalFilter, Ordered {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TokenValidator tokenValidator;

    @Value("${ignoreResources:**/actuator/health}")
    private String ignoreResources;

    OAuthTokenFilter(AuthenticateManager authenticateManager) {
        super(authenticateManager);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("OAuthTokenPolicy is filtering is started");
        if (super.skipCheckUrl(exchange, chain))
            return chain.filter(exchange);
        if (this.isAuthTokenEndpoint(exchange, chain)) {
            var client = super.getClient(exchange);
            super.validateClientSecret(client, HttpHeaderUtils.clientSecret(exchange.getRequest().getHeaders()));
            return chain.filter(exchange);
        }
        Client client = clientService
                .findClientById(clientId(exchange.getRequest().getHeaders()));
        Optional<ClientPolicy> policyOAuth =
                client.isPolicyExisted(PolicyConstants.OAUTH2_TOKEN_POLICY);
        if (policyOAuth.isEmpty()) {
            return chain.filter(exchange);
        }

        Claims claims = tokenUtils.getClaim(tokenUtils.getAuthorizationToken(exchange.getRequest()));
        tokenValidator.create(exchange, claims).validate();

        // Adding the CIF to the header from authenticated jwt token.
        String CIF_TOKEN= TokenUtils.ClaimUtils.getCif(claims);
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("CIF", CIF_TOKEN)
                        .build())
                .build();
        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.REWRITE_TOKEN_ORDER;
    }

}