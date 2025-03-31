package com.demo.gateway.service.auth.handler;

import com.demo.gateway.domain.GrantType;
import com.demo.gateway.domain.Token;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.domain.entity.ClientTokenLog;
import com.demo.gateway.dto.request.AuthRequestDTO;
import com.demo.gateway.dto.request.TokenGenerateRequest;
import com.demo.gateway.dto.response.APIResponse;
import com.demo.gateway.dto.response.AbstractResponseDTO;
import com.demo.gateway.dto.response.AuthResponseDTO;
import com.demo.gateway.dto.response.LoginDetailsResponseDTO;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import com.demo.gateway.http.UserProfileHttpClient;
import com.demo.gateway.service.AbstractAuthHandler;
import com.demo.gateway.service.ClientService;
import com.demo.gateway.service.ClientTokenLogService;
import com.demo.gateway.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
public class UserRefreshTokenHandler
        extends AbstractAuthHandler {

    private final TokenUtils tokenUtils;
    private final ClientTokenLogService clientTokenLogService;
    private final UserProfileHttpClient userProfileHttpClient;

    public UserRefreshTokenHandler(TokenUtils tokenUtils,
                                   ClientTokenLogService clientTokenLogService,
                                   UserProfileHttpClient userProfileHttpClient) {
        this.tokenUtils = tokenUtils;
        this.clientTokenLogService = clientTokenLogService;
        this.userProfileHttpClient = userProfileHttpClient;
    }


    @Override
    public AbstractAuthHandler getClazz(AuthRequestDTO authRequestDTO, Client client) {
        if ((GrantType.valueOf(authRequestDTO.getGrantType()).equals(GrantType.refresh_token))) {
            return this;
        } else return null;
    }

    @Override
    public Mono<AbstractResponseDTO> authenticate(TokenGenerateRequest tokenGenerateRequest, AuthRequestDTO authRequestDTO) {
        LoginDetailsResponseDTO loginToken = new LoginDetailsResponseDTO();
        String requestRefreshToken = this.getRequestRefreshToken(authRequestDTO);
        Claims claim = tokenUtils.getClaim(requestRefreshToken);
        ClientTokenLog clientTokenLog =
                clientTokenLogService.validateRefreshTokenThenRenew(claim.getId(),
                        authRequestDTO.getDeviceId());
        authRequestDTO.setUsername(clientTokenLog.getCif());
        tokenGenerateRequest.setId(clientTokenLog.getTokenId());
        tokenGenerateRequest.setUserId(authRequestDTO.getUsername());
        tokenGenerateRequest.setRoles("USER_ROLE");
        tokenGenerateRequest.setDeviceId(authRequestDTO.getDeviceId());
        Token token = tokenUtils.generateToken(tokenGenerateRequest);
        loginToken.setType(Token.TOKEN_TYPE);
        loginToken.setAccessToken(token.getAccessToken());
        loginToken.setRefreshToken(token.getRefreshToken());
        loginToken.setLastLoginDate(clientTokenLog.getModifiedAt());

        Mono<APIResponse> userProfileResponse = userProfileHttpClient.authenticate(authRequestDTO);
        Mono<AuthResponseDTO> tokenResponse = Mono.just(loginToken);
        return tokenResponse.zipWith(userProfileResponse)
                .map(tuple -> {
                    LoginDetailsResponseDTO data = new LoginDetailsResponseDTO();
                    data.setAccessToken(tuple.getT1().getAccessToken());
                    data.setRefreshToken(tuple.getT1().getRefreshToken());
                    data.setLastLoginDate(new Date());
                    APIResponse apiResponse = new APIResponse();
                    apiResponse.setCode("S0001");
                    apiResponse.setMessage("Success.");
                    apiResponse.setData(data);
                    return apiResponse;
                });
    }

    private String getRequestRefreshToken(AuthRequestDTO authRequestDTO) {
        if (Objects.isNull(authRequestDTO.getRefreshToken())) {
            throw new AppException(AppErrorCode.AUTH_INVALID_REFRESH_TOKEN);
        }
        return authRequestDTO.getRefreshToken();
    }
}
