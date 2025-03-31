package com.demo.payment.exception.code;

public enum AppErrorCode implements ErrorMessageCode {
    SERVICE_NOT_FOUND("PS001", "Service not found."),
    PAYMENT_AMOUNT_INVALID("PS002", "Payment amount is invalid."),
    AMOUNT_GT_ZERO("PS003", "Amount must be greater the zero"),
    PAYMENT_USER_INVALID("PS004", "Payment user is invalid."),
    PAYMENT_CURRENCY_INVALID("PS005", "Payment currency is invalid."),
    INSUFFICIENT_FUN("PS006", "Insufficient Account Balance"),
    PAYMENT_CONFIRM_EXPIRED("PS007", "Payment transaction is expired."),
    PAYMENT_ID_INVALID("PS008", "Payment Id is invalid."),
    GIFT_CODE_INVALID("PS009", "Gift code is invalid."),
    GIFT_CODE_EXPIRED("PS010", "Gift code is expired."),
    SAME_ACCOUNT("PS011", "Sender and receiver accounts cannot be the same."),
    ;

    String code;
    String message;

    AppErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
