package com.demo.gateway.http;

import com.demo.gateway.config.GlobalErrorAttributes;
import com.demo.gateway.constants.APIRequestHeaderConstants;
import com.demo.gateway.dto.request.AuthRequestDTO;
import com.demo.gateway.dto.request.PaymentAuthorizeRequestDTO;
import com.demo.gateway.dto.response.APIResponse;
import com.demo.gateway.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@RefreshScope
@Slf4j
public class UserProfileHttpClient {

    @Value("${user.profile.base-url}")
    private String userProfileUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;


    public Mono<APIResponse> authenticate(AuthRequestDTO authRequestDTO) {
        log.info("User ID: [{}] login.", authRequestDTO.getUsername());
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", authRequestDTO.getUsername());
        requestBody.put("password", authRequestDTO.getPassword());
        requestBody.put("grant_type", authRequestDTO.getGrantType());
        requestBody.put("is_relogin", authRequestDTO.isReLogin());
        return webClientBuilder.baseUrl(userProfileUrl)
                .build()
                .post()
                .uri("/api/v1.0/account/verify")
                .header(APIRequestHeaderConstants.DEVICE_ID, authRequestDTO.getDeviceId())
                .header(APIRequestHeaderConstants.APP_VERSION, authRequestDTO.getAppVersion())
                .header(APIRequestHeaderConstants.LOCATION, authRequestDTO.getLocation())
                .header(APIRequestHeaderConstants.LANGUAGE, authRequestDTO.getLanguage())
                .header(APIRequestHeaderConstants.DEVICE_INFO, authRequestDTO.getDeviceInfo())
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), response ->
                        response.bodyToMono(APIResponse.class)
                                .onErrorMap(error -> {
                                    throw new ServerException(GlobalErrorAttributes.DEFAULT_TITLE,
                                            HttpStatus.INTERNAL_SERVER_ERROR.value() + "", error.getMessage(),
                                            HttpStatus.INTERNAL_SERVER_ERROR);
                                })
                                .flatMap(error -> {
                                    throw new ServerException(error.getTitle(), error.getCode(), error.getMessage(),
                                            HttpStatus.resolve(response.statusCode().value()));
                                })
                )
                .bodyToMono(APIResponse.class);
    }

    public Mono<APIResponse> authorizePayment(PaymentAuthorizeRequestDTO paymentAuthorizeRequestDTO) {
        log.debug("Authorize pin is executing.");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("tpin", paymentAuthorizeRequestDTO.getTpin());
        requestBody.put("cif", paymentAuthorizeRequestDTO.getCif());
        log.info("Verify tpin for the user id:{}", paymentAuthorizeRequestDTO.getCif());
        return webClientBuilder.baseUrl(userProfileUrl)
                .build()
                .post()
                .uri("/api/v1.0/accounts/authorize")
                .header("cif", paymentAuthorizeRequestDTO.getCif())
                .header(APIRequestHeaderConstants.DEVICE_ID, paymentAuthorizeRequestDTO.getDeviceId())
                .header(APIRequestHeaderConstants.APP_VERSION, paymentAuthorizeRequestDTO.getAppVersion())
                .header(APIRequestHeaderConstants.LOCATION, paymentAuthorizeRequestDTO.getLocation())
                .header(APIRequestHeaderConstants.LANGUAGE, paymentAuthorizeRequestDTO.getLanguage())
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), response ->
                        response.bodyToMono(APIResponse.class)
                                .onErrorMap(error -> {
                                    throw new ServerException(GlobalErrorAttributes.DEFAULT_TITLE,
                                            HttpStatus.INTERNAL_SERVER_ERROR.value() +
                                                    "", error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                                })
                                .flatMap(error -> {
                                    throw new ServerException(error.getTitle(), error.getCode(), error.getMessage(),
                                            HttpStatus.resolve(response.statusCode().value()));
                                })
                ).bodyToMono(APIResponse.class);
    }

}
