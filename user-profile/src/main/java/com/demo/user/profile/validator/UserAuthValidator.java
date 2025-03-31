package com.demo.user.profile.validator;

public interface UserAuthValidator {
    default boolean isValidateOnLogin(UserValidatorPojo request){return true;}
    default boolean isValidateOnVerifyPin(UserValidatorPojo request){return true;}
    default int getRunningOrder() {
        return 100;
    }
    <T extends UserValidatorPojo> void validate(T request);
}
