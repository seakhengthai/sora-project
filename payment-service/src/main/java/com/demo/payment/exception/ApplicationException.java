package com.demo.payment.exception;

import com.demo.payment.exception.code.ErrorMessageCode;
import org.springframework.util.StringUtils;

public class ApplicationException extends RuntimeException {
    private ErrorMessageCode errorMessageCode;
    private String title;
    private String titleKh;
    private String message;
    private String messageKh;
    private String code;
    Object data;

    public ApplicationException(ErrorMessageCode errorMessageCode) {
        super(errorMessageCode.getCode());
        this.setError(errorMessageCode);
    }

    public ApplicationException(String code) {
        super(code);
        this.setError(code);
    }

    public ApplicationException(String code, String message) {
        super(code);
        this.setError(code, message);
    }

    public ApplicationException(String code, String message, Object data) {
        super(code);
        this.setError(code, message);
        this.data = data;
    }

    public ApplicationException(String code, String message, Object data, Throwable cause) {
        super(code, cause);
        this.setError(code, message);
        this.data = data;
    }

    public ApplicationException(String code, String message, Throwable cause) {
        super(code, cause);
        this.setError(code, message);
    }

    public ApplicationException(String code, Throwable cause) {
        super(code, cause);
        this.setError(code);
    }

    public ApplicationException(ErrorMessageCode errorMessageCode, String placeHolder, Object value) {
        super(errorMessageCode.getCode());
        this.code = errorMessageCode.getCode();
        this.message = StringUtils.replace(errorMessageCode.getMessage(), placeHolder, value.toString());
    }

    public ApplicationException(ErrorMessageCode errorMessageCode, Object data) {
        super(errorMessageCode.getCode());
        this.setError(errorMessageCode);
        this.data = data;
    }

    private void setError(String code, String message) {
        // TODO Getting from SysMessage
        this.code = code;
        this.message = message;
        this.messageKh = null;
    }

    private void setError(ErrorMessageCode errorMessageCode) {
        this.errorMessageCode = errorMessageCode;
        this.setError(errorMessageCode.getCode(), errorMessageCode.getMessage());
    }

    private void setError(String code) {
        this.setError(code, null);
    }

    public ErrorMessageCode getErrorMessageCode() {
        return this.errorMessageCode;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTitleKh() {
        return this.titleKh;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMessageKh() {
        return this.messageKh;
    }

    public String getCode() {
        return this.code;
    }

    public Object getData() {
        return this.data;
    }

    public String toString() {
        ErrorMessageCode var10000 = this.getErrorMessageCode();
        return "ApplicationException(errorMessageCode=" + var10000 + ", title=" + this.getTitle() + ", titleKh=" + this.getTitleKh() + ", message=" + this.getMessage() + ", messageKh=" + this.getMessageKh() + ", code=" + this.getCode()  + ", data=" + this.getData() + ")";
    }

    public ApplicationException() {
    }
}
