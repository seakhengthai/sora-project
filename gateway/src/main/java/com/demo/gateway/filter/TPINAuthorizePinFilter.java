package com.demo.gateway.filter;

import com.demo.gateway.constants.APIRequestHeaderConstants;
import com.demo.gateway.constants.FilterOrder;
import com.demo.gateway.constants.PolicyConstants;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.domain.entity.ClientPolicy;
import com.demo.gateway.dto.request.PaymentAuthorizeRequestDTO;
import com.demo.gateway.dto.response.UserPinVerifyResponseDTO;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import com.demo.gateway.filter.config.PaymentAuthorizeConfig;
import com.demo.gateway.http.UserProfileHttpClient;
import com.demo.gateway.service.AuthenticateManager;
import com.demo.gateway.service.ClientService;
import com.demo.gateway.utils.HttpHeaderUtils;
import com.demo.gateway.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TPINAuthorizePinFilter extends BaseGlobalFilter
        implements org.springframework.cloud.gateway.filter.GlobalFilter,
        Ordered {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final String TPIN = "TPIN";

    private final UserProfileHttpClient userProfileHttpClient;
    private final ClientService clientService;

    TPINAuthorizePinFilter(AuthenticateManager authenticateManager,
                           UserProfileHttpClient userProfileHttpClient,
                           ClientService clientService) {
        super(authenticateManager);
        this.userProfileHttpClient = userProfileHttpClient;
        this.clientService = clientService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("TPIN authorization is filtering.");
        if (super.skipCheckUrl(exchange, chain))
            return chain.filter(exchange);
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        Client client = clientService
                .findClientById(HttpHeaderUtils.clientId(exchange.getRequest().getHeaders()));
        if (this.shouldVerifyPaymentAuthorize(client, path)) {
            String CIF = request.getHeaders().getFirst(TokenUtils.CIF);
            PaymentAuthorizeRequestDTO paymentAuthorizeRequestDTO = PaymentAuthorizeRequestDTO.builder()
                    .cif(CIF)
                    .deviceId(request.getHeaders().getFirst(APIRequestHeaderConstants.DEVICE_ID))
                    .appVersion(request.getHeaders().getFirst(APIRequestHeaderConstants.APP_VERSION))
                    .location(request.getHeaders().getFirst(APIRequestHeaderConstants.LOCATION))
                    .language(request.getHeaders().getFirst(APIRequestHeaderConstants.LANGUAGE))
                    .build();
            String authorizeVal;
            if (request.getHeaders().containsKey(TPIN)) {
                authorizeVal = request.getHeaders().get(TPIN).get(0);
                paymentAuthorizeRequestDTO.setTpin(authorizeVal);
            }else{
                throw new AppException(AppErrorCode.AUTH_INVALID_PIN);
            }
            return userProfileHttpClient
                    .authorizePayment(paymentAuthorizeRequestDTO)
                    .flatMap(apiResponse -> {
                        ObjectMapper om = new ObjectMapper();
                        UserPinVerifyResponseDTO body =
                                om.convertValue(apiResponse.getData(), UserPinVerifyResponseDTO.class);
                        exchange.getRequest()
                                .mutate()
                                .header(APIRequestHeaderConstants.PAUTH_TOKEN, body.getAuthorizeToken())
                                .build();
                        return chain.filter(exchange);
                    });
        }
        return chain.filter(exchange);
    }

    private boolean shouldVerifyPaymentAuthorize(Client client, String path) {
        Optional<ClientPolicy> first =
                client.isPolicyExisted(PolicyConstants.TPIN_AUTHORIZE_POLICY);
        if (first.isEmpty())
            return false;
        PaymentAuthorizeConfig config =
                parseConfiguration(first.get().getPolicyConfig());
        List<String> configurePath = config.getPaths();
        for (String cPath :
                configurePath) {
            PathPattern pathInDB = PathPatternParser.defaultInstance.parse(cPath);
            PathContainer pathContainer = PathContainer.parsePath(path);
            if (pathInDB.matches(pathContainer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return FilterOrder.PAYMENT_PIN_FILTER;
    }

    public PaymentAuthorizeConfig parseConfiguration(String jsonConfiguration) {
        try {
            return mapper.readerFor(PaymentAuthorizeConfig.class).readValue(jsonConfiguration);
        } catch (Exception e) {
            log.error("ParseConfiguration", e);
        }
        return null;
    }
}
