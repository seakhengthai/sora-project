package com.demo.gateway.config;

import com.demo.gateway.exception.GatewayException;
import com.demo.gateway.exception.code.ServerMessageCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    public static String code = "code";
    public static String TRACE_ID = "trace_id";
    public static String message = "message";
    public static String status = "status";
    public static String title = "title";
    public static String DEFAULT_MSG = "Ops! Something went wrong.";
    public static String DEFAULT_TITLE = "Server Issue";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = new HashMap<>();
        Throwable exception = (this.getError(request));
        if (exception instanceof GatewayException ex) {
            map.put(code, ex.getCode());
            map.put(message, ex.getMessage());
            map.put(status, ex.getStatus());
            map.put(title, ex.getTitle());
            return map;
        } else if (exception instanceof ConnectException) {
            map.put(code, ServerMessageCode.SERVER_UNAVAILABLE.getCode());
            map.put(message, ServerMessageCode.SERVER_UNAVAILABLE.getMessage());
            map.put(status, HttpStatus.SERVICE_UNAVAILABLE);
            map.put(status, HttpStatus.SERVICE_UNAVAILABLE);
            return map;
        } else if (exception instanceof ResponseStatusException) {
            if (((ResponseStatusException) exception).getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                map.put(code, ServerMessageCode.NOT_FOUND.getCode());
                map.put(title,DEFAULT_TITLE);
                map.put(message, ServerMessageCode.NOT_FOUND.getMessage());
                map.put(status, HttpStatus.NOT_FOUND);
                return map;
            }
        }
        map.put(code, ServerMessageCode.SERVER_ERROR.getCode());
        map.put(message, DEFAULT_MSG);
        map.put(status, HttpStatus.INTERNAL_SERVER_ERROR);
        map.put(title, DEFAULT_TITLE);
        return map;
    }
}