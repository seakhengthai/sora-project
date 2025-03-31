package com.demo.gateway.exception;

import com.demo.gateway.exception.code.AppErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends GatewayException {

    private AppErrorCode appErrorCode;

    public AppException() {
    }


    public AppException(String code, String message) {
        super(code, message);
        this.setStatus(HttpStatus.UNAUTHORIZED);
    }

    public AppException(AppErrorCode appErrorCode) {
        super(appErrorCode);
        this.setStatus(HttpStatus.UNAUTHORIZED);
    }

    public AppException(AppErrorCode appErrorCode, HttpStatus status) {
        super(appErrorCode);
        this.setStatus(status);
    }

}
