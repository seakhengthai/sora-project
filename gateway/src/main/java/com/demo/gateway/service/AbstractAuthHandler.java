package com.demo.gateway.service;

import com.demo.gateway.domain.Channel;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.dto.request.AuthRequestDTO;
import com.demo.gateway.dto.request.TokenGenerateRequest;
import com.demo.gateway.dto.response.AbstractResponseDTO;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;


@Setter
@Getter
@NoArgsConstructor
@Slf4j
public abstract class AbstractAuthHandler extends BaseClientService {


    private ClientService clientService;

    public AbstractAuthHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    public abstract AbstractAuthHandler getClazz(AuthRequestDTO authRequestDTO, Client client);

    public abstract <R extends AbstractResponseDTO> Mono<R> authenticate(TokenGenerateRequest tokenGenerateRequest,
                                                                         AuthRequestDTO authRequestDTO );

    public void validateClientInfo(Client client, AuthRequestDTO authRequestDTO) {
        this.validateClientSecret(client, authRequestDTO.getClientSecret());
        this.validateClientGrantType(client, authRequestDTO);
    }

    public TokenGenerateRequest verifyClientDetails(Client client, AuthRequestDTO authRequestDTO) {
        if (authRequestDTO.getClientId() == null || authRequestDTO.getClientSecret() == null) {
            throw new AppException(AppErrorCode.AUTH_INVALID_HEADER, HttpStatus.BAD_REQUEST);
        }
        this.validateClientInfo(client, authRequestDTO);
        return TokenGenerateRequest.builder()
                .clientId(authRequestDTO.getClientId())
                .client(client)
                .accessTokenExpired(client.getAccessTokenExpired())
                .refreshTokenExpired(client.getRefreshTokenExpired())
                .channel(Channel.valueOf(client.getChannel()))
                .build();
    }
}
