package com.demo.gateway.exception;

import com.demo.gateway.exception.code.ErrorMessageCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
@ToString
@Setter
public abstract class GatewayException extends RuntimeException {

    private HttpStatus status;
    private String code;
    private String message;
    private String title;
    private ErrorMessageCode errorMessageCode;


    public HttpStatus getStatus() {
        return status;
    }

    public GatewayException(String code, String message) {
        this.message = message;
        this.code = code;
    }

    public GatewayException(String code,String message,HttpStatus status){
        this.message = message;
        this.code = code;
        this.status=status;
    }
    public GatewayException(ErrorMessageCode errorMessageCode) {
        this.errorMessageCode = errorMessageCode;
        this.message=errorMessageCode.getMessage();
        this.code=errorMessageCode.getCode();
    }

    public GatewayException(ErrorMessageCode  errorMessageCode,HttpStatus status){
        this.errorMessageCode = errorMessageCode;
        this.status=status;
        this.code=errorMessageCode.getCode();
        this.message=errorMessageCode.getMessage();
    }

    public GatewayException(String title,String code,String message,HttpStatus status){
        this.message = message;
        this.code = code;
        this.status=status;
        this.title=title;
    }
}