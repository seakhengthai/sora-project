package com.demo.gateway.service.auth.handler;

import com.demo.gateway.domain.GrantType;
import com.demo.gateway.domain.Token;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.dto.request.AuthRequestDTO;
import com.demo.gateway.dto.request.TokenGenerateRequest;
import com.demo.gateway.dto.response.APIResponse;
import com.demo.gateway.dto.response.AbstractResponseDTO;
import com.demo.gateway.dto.response.AuthResponseDTO;
import com.demo.gateway.service.AbstractAuthHandler;
import com.demo.gateway.service.ClientService;
import com.demo.gateway.utils.TokenUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Component
public class ClientCredentialsAuthHandler extends AbstractAuthHandler {

    private final TokenUtils tokenUtils;

    public ClientCredentialsAuthHandler(ClientService clientService, TokenUtils tokenUtils) {
        super(clientService);
        this.tokenUtils = tokenUtils;
    }

    @Override
    public AbstractAuthHandler getClazz(AuthRequestDTO authRequestDTO, Client client) {
        if (GrantType.valueOf(authRequestDTO.getGrantType()).equals(GrantType.client_credentials)) {
            return this;
        } else return null;
    }

    @Override
    public Mono<AbstractResponseDTO> authenticate(TokenGenerateRequest tokenGenerateRequest, AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        tokenGenerateRequest.setDeviceId(getDeviceId(authRequestDTO));
        tokenGenerateRequest.setRoles(tokenGenerateRequest.getClient().getRoles());
        Token token = tokenUtils.generateOnlyAccessToken(tokenGenerateRequest);
        authResponseDTO.setType(token.getType());
        authResponseDTO.setAccessToken(token.getAccessToken());
        APIResponse apiResponse = new APIResponse();
        apiResponse.setData(authResponseDTO);
        return Mono.just(apiResponse);
    }
    private String getDeviceId(AuthRequestDTO authRequestDTO) {
        if (StringUtils.hasLength(authRequestDTO.getDeviceId()))
            return authRequestDTO.getDeviceId();
        return "xxx";
    }
}
