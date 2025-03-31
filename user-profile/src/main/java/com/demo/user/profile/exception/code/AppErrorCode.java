package com.demo.user.profile.exception.code;

public enum AppErrorCode implements ErrorMessageCode {
    USER_NOT_ALLOWED("UP001", "User is not allowed to enable config!"),
    VERIFY_TYPE_NOT_VALID("UP002", "Verify type is not valid!"),
    CREDENTIAL_NOT_VALID("UP003", "User credential is not valid!"),
    AUTHENTICATION_FAIL("UP004", "User authentication failed! You may try other method"),
    CONFIG_AMOUNT_NOT_ALLOWED("UP005", "User config amount is not allowed to enable config!"),
    INVALID_USER ("UP006", "Invalid user"),
    USER_BLOCKED("UP007", "The user has been blocked."),
    USER_INACTIVE("UP008", "THe user has been inactive."),
    INVALID_GRANT_TYPE("UP009", "Invalid request grant type."),
    INVALID_PIN("UP010", "Incorrect PIN."),
    INVALID_ACCOUNT("UP011", "Invalid account."),
    INSUFFICIENT_BALANCE("UP012", "Insufficient balance"),

    SAME_ACCOUNT("UP013", "Sender and receiver accounts cannot be the same."),
    INVALID_AMOUNT("UP014", "Invalid amount."),
    DUPLICATE_EXT_REF("UP015", "Duplicate external reference."),
    ACCOUNT_STATEMENT_EMPTY("UP016", "Account statement is empty"),
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
