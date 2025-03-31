package com.demo.user.profile.validator.impl;

import com.demo.user.profile.domain.Status;
import com.demo.user.profile.domain.entity.UserAuthDetail;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.exception.code.AppErrorCode;
import com.demo.user.profile.validator.UserAuthValidator;
import com.demo.user.profile.validator.UserValidatorPojo;
import org.springframework.stereotype.Component;

@Component("userAuthStatusValidator")
public class UserAuthStatusValidator implements UserAuthValidator {

    @Override
    public int getRunningOrder() {
        return 1;
    }

    @Override
    public <T extends UserValidatorPojo> void validate(T request) {
        UserAuthDetail userAuth = request.getAuthDetails();
        if (Status.INACTIVE.equals(userAuth.getStatus())) {
            throw new ApplicationException(AppErrorCode.USER_INACTIVE);
        } else if (Status.BLOCKED.equals(userAuth.getStatus())) {
            throw new ApplicationException(AppErrorCode.USER_BLOCKED);
        }
    }
}
