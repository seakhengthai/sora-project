package com.demo.gateway.controller;


import com.demo.gateway.constants.APIRequestHeaderConstants;
import com.demo.gateway.domain.HashFields;
import com.demo.gateway.dto.request.AuthRequestDTO;
import com.demo.gateway.dto.response.APIResponse;
import com.demo.gateway.filter.IntegrityMessageWebFilter;
import com.demo.gateway.logging.HttpRequest;
import com.demo.gateway.service.OAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({"/oauth/", "api/v1.0/oauth"})
@AllArgsConstructor
@Validated
public class OAuthController {

    private final OAuthService authService;

    private final Logger logger = LoggerFactory.getLogger(OAuthController.class);

    private final IntegrityMessageWebFilter messageWebFilter;


    @PostMapping("/token")
    public ResponseEntity<?> grantToken(
            ServerWebExchange exchange,
            @RequestHeader(APIRequestHeaderConstants.CLIENT_ID) String clientId,
            @RequestHeader(APIRequestHeaderConstants.CLIENT_SECRET) String clientSecret,
            @RequestHeader(value = APIRequestHeaderConstants.DEVICE_ID) String deviceId,
            @RequestHeader(value = APIRequestHeaderConstants.CLIENT_TIMESTAMP) String clientReqTimestamp,
            @RequestHeader(value = APIRequestHeaderConstants.APP_VERSION, required = false) String appVersion,
            @RequestHeader(value = APIRequestHeaderConstants.LOCATION, required = false) String location,
            @RequestHeader(value = APIRequestHeaderConstants.LANGUAGE, required = false) String langage,
            @RequestHeader(value = APIRequestHeaderConstants.API_KEY) String apiKey,
            @RequestHeader(value = APIRequestHeaderConstants.DEVICE_INFO, required = false) String deviceInfo,
            @RequestBody @Valid AuthRequestDTO authRequestDTO) {

        this.logBefore(authRequestDTO, deviceId, deviceInfo);
        authRequestDTO.setClientId(clientId);
        authRequestDTO.setClientSecret(clientSecret);
        authRequestDTO.setDeviceId(deviceId);
        authRequestDTO.setAppVersion(appVersion);
        authRequestDTO.setLocation(location);
        authRequestDTO.setXApiKey(apiKey);
        authRequestDTO.setLanguage(langage);
        authRequestDTO.setDeviceInfo(deviceInfo);

        // adding cif into the request
        exchange.mutate()
                .request(exchange.getRequest().mutate().header(HttpRequest.CIF_KEY
                        , authRequestDTO.getUsername()).build())
                .build();

        Mono<APIResponse> authenticate = authService.authenticate(authRequestDTO);
        this.logAfter();
        HashFields hashFields
                = HashFields.builder()
                .clientId(clientId)
                .deviceId(deviceId)
                .requestMethod("POST")
                .requestTime(clientReqTimestamp)
                .statusCode("200")
                .requestUrl("/oauth/token")
                .build();
        return ResponseEntity.ok()
                .header(messageWebFilter.integrityEnabledKey, messageWebFilter.integrityEnabledValue)
                .header(IntegrityMessageWebFilter.headerDigestKey, messageWebFilter.calculateHash(hashFields))
                .body(authenticate);
    }


    private void logBefore(AuthRequestDTO authRequestDTO, String deviceId, String deviceInfo) {
        logger.info("Login is started with user cif : [{}], device_id: [{}], grant_type :[{}], device info :[{}]",
                authRequestDTO.getUsername(), deviceId, authRequestDTO.getGrantType(), deviceInfo);

    }

    private void logAfter() {
        logger.info("Login is completed.");
    }

}
