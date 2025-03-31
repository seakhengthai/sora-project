package com.demo.gateway.filter;

import com.demo.gateway.domain.HashFields;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class IntegrityMessageWebFilter implements GlobalFilter {


    @Value("${gateway.secret-key}")
    private String key;

    @Value("${gateway.integrity.enabled:true}")
    public boolean integrityEnabled;

    public static String integrityEnabledValue;

    public static String integrityEnabledKey = "Integrity-Enabled";

    public static String headerDigestKey = "Gateway-Digest";

    @PostConstruct
    public void initHashValue() {
        integrityEnabledValue = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(String.valueOf(integrityEnabled));
        log.info("Has value enabled with value original [{}] and hashed [{}]",integrityEnabled,
                IntegrityMessageWebFilter.integrityEnabledValue);
    }

    public String calculateHash(HashFields hashFields) {
        if (integrityEnabled) {
            String hashString = "(Client-Id): %s, (Device ID): %s, (Request-Time): %s, (Request-URI): %s, (Status Code): %s, (Http Method): %s ";
            String finalFormat = String.format(hashString, hashFields.getClientId(), hashFields.getDeviceId(), hashFields.getRequestTime(), hashFields.getRequestUrl(), hashFields.getStatusCode(), hashFields.getRequestMethod());
            String hashValue = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(finalFormat);
            log.trace("HashFields : {}, hashValued : {}", hashFields, hashValue);
            StringBuilder sb = new StringBuilder("HmacSHA256=");
            return sb.append(hashValue).toString();
        }
        return "";
    }

    public String calculateHashRequest(HashFields hashFields) {
        String hashString = "(Client-Id): %s, (Device ID): %s, (Request-Time): %s, (Request-URI): %s, (Http Method): %s,  (AppVersion): %s, (TPIN): %s";
        String finalFormat = String.format(hashString, hashFields.getClientId(),
                hashFields.getDeviceId(),
                hashFields.getRequestTime(),
                hashFields.getRequestUrl(),
                hashFields.getRequestMethod(),
                hashFields.getAppVersion(),
                hashFields.getTpin());
        String hashValue = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(finalFormat);
        log.trace("HashFields : {}, hashValued : {} for request.", hashFields, hashValue);
        return hashValue;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("On global response headers digest for integrity check signature all routes");
        HttpHeaders headers = exchange.getResponse().getHeaders();
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    headers.add(integrityEnabledKey, integrityEnabledValue);
                    headers.add(headerDigestKey, this.calculateHash(HashFields.fromExchange(exchange)));
                }));

    }
}
