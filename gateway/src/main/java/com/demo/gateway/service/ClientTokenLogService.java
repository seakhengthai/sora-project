package com.demo.gateway.service;


import com.demo.gateway.domain.entity.ClientTokenLog;
import com.demo.gateway.dto.request.TokenGenerateRequest;

public interface ClientTokenLogService {

    ClientTokenLog log(TokenGenerateRequest tokenGenerateRequest);

    ClientTokenLog validateRefreshTokenThenRenew(String tokenId,String deviceId);
}
