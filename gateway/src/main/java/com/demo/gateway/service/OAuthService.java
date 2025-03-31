package com.demo.gateway.service;

import com.demo.gateway.dto.request.AuthRequestDTO;
import com.demo.gateway.dto.response.APIResponse;
import reactor.core.publisher.Mono;

public interface OAuthService {
    <R extends APIResponse> Mono<R> authenticate(AuthRequestDTO authRequestDTO);
}
