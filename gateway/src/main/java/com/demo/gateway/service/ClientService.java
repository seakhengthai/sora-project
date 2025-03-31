package com.demo.gateway.service;

import com.demo.gateway.domain.entity.Client;

public interface ClientService {
    Client findClientById(String clientId);
}
