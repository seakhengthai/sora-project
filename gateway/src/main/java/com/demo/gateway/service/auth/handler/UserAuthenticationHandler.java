package com.demo.gateway.service.auth.handler;

import com.demo.gateway.domain.Channel;
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
import com.demo.gateway.http.UserProfileHttpClient;
import com.demo.gateway.service.AbstractAuthHandler;
import com.demo.gateway.service.ClientTokenLogService;
import com.demo.gateway.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserAuthenticationHandler extends AbstractAuthHandler {

    private final TokenUtils tokenUtils;
    private final ClientTokenLogService clientTokenLogService;
    private final UserProfileHttpClient userProfileHttpClient;

    public UserAuthenticationHandler(TokenUtils tokenUtils,
                                     ClientTokenLogService clientTokenLogService,
                                     UserProfileHttpClient userProfileHttpClient) {
        this.tokenUtils = tokenUtils;
        this.clientTokenLogService = clientTokenLogService;
        this.userProfileHttpClient = userProfileHttpClient;
    }

    @Override
    public AbstractAuthHandler getClazz(AuthRequestDTO authRequestDTO, Client client) {
        if ((GrantType.valueOf(authRequestDTO.getGrantType()).equals(GrantType.password))) {
            return this;
        } else return null;
    }

    @Override
    public Mono<AbstractResponseDTO> authenticate(TokenGenerateRequest tokenGenerateRequest, AuthRequestDTO authRequestDTO) {
        Mono<APIResponse> userProfileResponse = userProfileHttpClient.authenticate(authRequestDTO);
        tokenGenerateRequest.setUserId(authRequestDTO.getUsername());
        tokenGenerateRequest.setChannel(Channel.valueOf(tokenGenerateRequest.getClient().getChannel()));
        tokenGenerateRequest.setRoles("USER_ROLE");
        tokenGenerateRequest.setDeviceId(authRequestDTO.getDeviceId());
        Mono<AuthResponseDTO> tokenResponse = Mono.just(new AuthResponseDTO());
        ObjectMapper OM = new ObjectMapper();
        return tokenResponse.zipWith(userProfileResponse)
            .map((tuple) -> {
                Map<String, Object> profile = OM.convertValue(tuple.getT2().getData(), HashMap.class);
                ClientTokenLog clientTokenLog = clientTokenLogService.log(tokenGenerateRequest);
                tokenGenerateRequest.setId(clientTokenLog.getTokenId());
                tokenGenerateRequest.setCif(String.valueOf(profile.get("cif")));
                Token token = tokenUtils.generateToken(tokenGenerateRequest);
                LoginDetailsResponseDTO responseDTO = new LoginDetailsResponseDTO();
                responseDTO.setLastLoginDate(clientTokenLog.getModifiedAt());
                responseDTO.setAccessToken(token.getAccessToken());
                responseDTO.setRefreshToken(token.getRefreshToken());
                responseDTO.setType(token.getType());
                responseDTO.setProfile(profile.getOrDefault("user_profile", ""));
                APIResponse apiResponse = new APIResponse();
                apiResponse.setCode("S0001");
                apiResponse.setMessage("Success.");
                apiResponse.setData(responseDTO);
                return apiResponse;
            });
    }
}
