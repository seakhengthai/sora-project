package com.demo.gateway.service.impl;

import com.demo.gateway.domain.Status;
import com.demo.gateway.domain.entity.ClientTokenLog;
import com.demo.gateway.dto.request.TokenGenerateRequest;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import com.demo.gateway.repository.ClientTokenLogRepository;
import com.demo.gateway.service.ClientTokenLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ClientTokenLogServiceImpl implements ClientTokenLogService {

    private final ClientTokenLogRepository tokenLogRepository;

    @Override
    public ClientTokenLog log(TokenGenerateRequest tokenGenerateRequest) {
        log.debug("Create new token log with id for user id:{}", tokenGenerateRequest.getUserId());
        Optional<ClientTokenLog> byTokenIdAndStatus =
                tokenLogRepository.findByCifAndStatus(tokenGenerateRequest.getUserId(), Status.ACTIVE);
        if (byTokenIdAndStatus.isEmpty()) {
            ClientTokenLog clientTokenLog = ClientTokenLog.builder()
                    .clientId(tokenGenerateRequest.getClientId())
                    .cif(tokenGenerateRequest.getUserId())
                    .tokenId(this.getTokenId())
                    .deviceId(tokenGenerateRequest.getDeviceId())
                    .build();
            return tokenLogRepository.save(clientTokenLog);
        }
        ClientTokenLog clientTokenLog = byTokenIdAndStatus.get();
        clientTokenLog.setTokenId(this.getTokenId());
        clientTokenLog.setDeviceId(tokenGenerateRequest.getDeviceId());
        return tokenLogRepository
                .save(byTokenIdAndStatus.get());
    }

    @Override
    public ClientTokenLog validateRefreshTokenThenRenew(String tokenId, String deviceId) {
        log.debug("Refresh token id from id:{} and device id:[{}]", tokenId, deviceId);
        Optional<ClientTokenLog> byTokenIdAndStatus =
                tokenLogRepository.findByTokenIdAndStatusAndDeviceId(tokenId, Status.ACTIVE,deviceId);
        ClientTokenLog clientTokenLog = byTokenIdAndStatus
                .orElseThrow(() -> new AppException(AppErrorCode.AUTH_INVALID_REFRESH_TOKEN));
        clientTokenLog.setTokenId(this.getTokenId());
        return tokenLogRepository.save(clientTokenLog);
    }

    private String getTokenId() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }
}
