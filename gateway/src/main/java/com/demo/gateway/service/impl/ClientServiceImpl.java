package com.demo.gateway.service.impl;

import com.demo.gateway.domain.Status;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import com.demo.gateway.repository.ClientRepository;
import com.demo.gateway.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@RefreshScope
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    @Cacheable(value = "clients", key = "#id")
    public Client findClientById(String id) {
        log.info("Finding the client with id:{}", id);
        return clientRepository.findAll()
                .stream()
                .filter(c -> c.getId().equals(id) && c.getStatus().equals(Status.ACTIVE))
                .findFirst()
                .orElseThrow(() -> new AppException(AppErrorCode.AUTH_INVALID_CLIENT_CREDENTIALS));
    }
}
