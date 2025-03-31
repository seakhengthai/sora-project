package com.demo.gateway.service.impl;

import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.dto.request.AuthRequestDTO;
import com.demo.gateway.dto.request.TokenGenerateRequest;
import com.demo.gateway.dto.response.AbstractResponseDTO;
import com.demo.gateway.service.AbstractAuthHandler;
import com.demo.gateway.service.ClientService;
import com.demo.gateway.service.OAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OAuthServiceImpl implements OAuthService {

    private final List<AbstractAuthHandler> abstractAuthHandlerList;
    private ClientService clientService;

    public Mono<AbstractResponseDTO> authenticate(AuthRequestDTO authRequestDTO) {
        Client client = clientService.findClientById(authRequestDTO.getClientId());
        return getLoginHandler(client, authRequestDTO);
    }

    Mono<AbstractResponseDTO> getLoginHandler(Client client, AuthRequestDTO authRequestDTO) {
        log.info("Device ID: [{}] is login.",authRequestDTO.getDeviceId());
        for (AbstractAuthHandler abstractAuthHandler : abstractAuthHandlerList) {
            TokenGenerateRequest tokenGenerateRequest = abstractAuthHandler
                    .verifyClientDetails(client, authRequestDTO);
            AbstractAuthHandler abstractAuthHandlerResult = abstractAuthHandler.getClazz(authRequestDTO, client);
            if (abstractAuthHandlerResult != null)
                return abstractAuthHandlerResult.authenticate(tokenGenerateRequest, authRequestDTO);
        }
        throw new RuntimeException("No authentication handler is found.");
    }
}