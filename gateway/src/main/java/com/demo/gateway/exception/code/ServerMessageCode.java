package com.demo.gateway.exception.code;

public enum ServerMessageCode implements ErrorMessageCode{
    SERVER_ERROR("E0001","Server connection refused."),
    SERVER_ERROR_DOWNSTREAM("E0002","Response from downstream service."),
    SERVER_ERROR_INTERNAL("E0003","Internal server error."),
    SERVER_UNAVAILABLE("E0004","Server is temporary unavailable."),
    NOT_FOUND("E0404","Not found.");

    String code;
    String message;


    ServerMessageCode(String code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.prefix()+code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
