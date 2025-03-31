package com.demo.gateway.dto.request;

import com.demo.gateway.domain.Channel;
import com.demo.gateway.domain.entity.Client;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class TokenGenerateRequest {
    String id;
    Long accessTokenExpired;
    Long refreshTokenExpired;
    String roles;
    String clientId;
    String cif;
    String userId;
    Channel channel;
    String deviceId;
    Client client;
}
