package com.demo.gateway.service;

import com.demo.gateway.constants.APIRequestHeaderConstants;
import com.demo.gateway.domain.Status;
import com.demo.gateway.domain.entity.API;
import com.demo.gateway.domain.entity.Client;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import com.demo.gateway.utils.APIUtilsPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;


@Service
public class ClientAPIAuthorizeAPI {

    @Autowired
    private APIUtilsPattern apiUtilsPattern;

    @Autowired
    private APIService apiService;

    private void validateIfClientAllowToAccessTheApi(Client client, API matchingApi) {
        var first =
                client.getClientAPI()
                        .getClientPublishAPIS()
                        .stream()
                        .filter(clientPublishAPI -> clientPublishAPI.getApiCode()
                                .equals(matchingApi.getCode())
                                && clientPublishAPI.getStatus().equals(Status.ACTIVE))
                        .findFirst();
        if (first.isEmpty()) {
            throw new AppException(AppErrorCode.API_NOT_REGISTER);
        }
    }

    public void validateMatchingAPI(Client client, ServerWebExchange exchange) {
        List<API> apisByRequestClient = apiService.findAll();
        ServerHttpRequest request = exchange.getRequest();
        API matchingApi = apiUtilsPattern
                .getMatchingApi(request.getPath().value(),
                        request.getMethod(), apisByRequestClient);
        this.validateIfClientAllowToAccessTheApi(client, matchingApi);
        exchange.getRequest().mutate()
                .header(APIRequestHeaderConstants.INTERNAL_API_CODE, matchingApi.getCode())
                .build();
    }

}
