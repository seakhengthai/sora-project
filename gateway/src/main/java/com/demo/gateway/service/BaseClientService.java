package com.demo.gateway.service;

import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.dto.request.AuthRequestDTO;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import com.demo.gateway.utils.APIManagerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BaseClientService {

    @Autowired
    private APIManagerUtils apiManagerUtils;

    public void validateClientSecret(Client client, String secret) {
        String secretString = client.getSecret();
        if (!secretString
                .equals(apiManagerUtils.compareEncryption(client.getId(), secret))) {
            throw new AppException(AppErrorCode.AUTH_INVALID_CLIENT_CREDENTIALS);
        }
    }

    public void validateClientGrantType(Client client, AuthRequestDTO authRequestDTO) {
        String requestGrantType = authRequestDTO.getGrantType();
        Optional<String> grantTypeOptional = client.getGrantTypes()
                .stream()
                .filter(grantType -> grantType.equals(requestGrantType)).findFirst();
        if (grantTypeOptional.isEmpty()) {
            throw new AppException(AppErrorCode.AUTH_GRANT_TYPE_INVALID);
        }
    }


}
