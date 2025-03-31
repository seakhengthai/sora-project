package com.demo.gateway.exception;

import com.demo.gateway.exception.code.ErrorMessageCode;
import com.demo.gateway.exception.code.ServerMessageCode;
import org.springframework.http.HttpStatus;

public class ServerException extends GatewayException{

    ServerMessageCode serverMessageCode;
    HttpStatus status;
    public ServerException() {
    }

    public ServerException(String code, String message) {
        super(code, message);
    }

    public ServerException(String code, String message,HttpStatus status) {
        super(code, message,status);
    }

    public ServerException(ErrorMessageCode errorMessageCode) {
        super(errorMessageCode);
    }
    public ServerException(ErrorMessageCode errorMessageCode,HttpStatus httpStatus) {
        super(errorMessageCode,httpStatus);
    }

    public ServerException(String title,String code, String message,HttpStatus status) {
        super(title, code, message,status);
    }

}