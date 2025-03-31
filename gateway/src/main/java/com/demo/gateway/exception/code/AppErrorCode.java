package com.demo.gateway.exception.code;

public enum AppErrorCode implements ErrorMessageCode {

    AUTH_INVALID_HEADER("M0001", "Http request header is invalid."),
    AUTH_INVALID_CLIENT_CREDENTIALS("M0002", "Client details is invalid."),
    AUTH_NO_PERMISSION("M0003", "Access denied."),
    AUTH_APIKEY_INVALID("M0004", "API key is invalid."),
    AUTH_GRANT_TYPE_INVALID("M0005", "grant_type is invalid."),
    AUTH_INVALID_TOKEN("M0006", "Access token is invalid."),
    AUTH_INVALID_TOKEN_DEVICE("M0011", "Access token or device is invalid."),
    AUTH_INVALID_REFRESH_TOKEN("M0009", "Refresh token is invalid."),
    AUTH_INVALID_PIN("E0011", "Unauthorized the payment."),
    AUTH_INVALID_SCOPE("M00014", "Request service is unauthorized."),
    API_NOT_FOUND("AM-0001", "API is not found."),
    API_NOT_REGISTER("AM-0002", "Client is unauthorized to access."),
    API_NOT_MATCHING("AM-0003", "Request API is not existed.");

    String code;
    String message;



    AppErrorCode(String code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.prefix() + code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
