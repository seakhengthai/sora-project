package com.demo.gateway.service;

import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.utils.HttpHeaderUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class AuthenticateManager extends BaseClientService {

    @Autowired
    private ClientService clientService;
    private String apiKey;
    private String clientId;
    private String clientSecret;

    private HttpHeaders httpHeaders;

    public AuthenticateManager create(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
    }


    public Client getClient() {
        this.setClientId(HttpHeaderUtils.clientId(httpHeaders));
        return clientService.findClientById(this.getClientId());
    }
}
