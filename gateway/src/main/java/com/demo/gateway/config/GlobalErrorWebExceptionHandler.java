package com.demo.gateway.config;

import com.demo.gateway.domain.HashFields;
import com.demo.gateway.filter.IntegrityMessageWebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Order(-2)
@Slf4j
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
    private final MediaType applicationJson = MediaType.APPLICATION_JSON;

    private final IntegrityMessageWebFilter messageWebFilter;

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer configurer,
                                          IntegrityMessageWebFilter messageWebFilter) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(configurer.getWriters());
        this.messageWebFilter = messageWebFilter;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        Map<String, Object> errorPropertiesMap = getErrorAttributes(request,
                ErrorAttributeOptions.defaults());

        HttpStatus httpStatus = (HttpStatus) errorPropertiesMap.get(GlobalErrorAttributes.status);
        String message = (String) errorPropertiesMap.get(GlobalErrorAttributes.message);
        String code = errorPropertiesMap.get(GlobalErrorAttributes.code).toString();

        String traceId = errorPropertiesMap.getOrDefault(GlobalErrorAttributes.TRACE_ID,
                System.currentTimeMillis()).toString();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put(GlobalErrorAttributes.code, code);
        responseMap.put(GlobalErrorAttributes.message, message);
        responseMap.put(GlobalErrorAttributes.TRACE_ID, traceId);
        if (Objects.nonNull(errorPropertiesMap.get(GlobalErrorAttributes.title))) {
            responseMap.put(GlobalErrorAttributes.title, errorPropertiesMap.get(GlobalErrorAttributes.title));
        }

        HashFields hashFields = HashFields.fromExchange(request.exchange());
        hashFields.setStatusCode(httpStatus.value() + "");
        return ServerResponse.status(httpStatus)
                .header(messageWebFilter.integrityEnabledKey, messageWebFilter.integrityEnabledValue)
                .header(IntegrityMessageWebFilter.headerDigestKey, messageWebFilter.calculateHash(hashFields))
                .contentType(applicationJson)
                .body(BodyInserters.fromValue(responseMap));
    }
}
