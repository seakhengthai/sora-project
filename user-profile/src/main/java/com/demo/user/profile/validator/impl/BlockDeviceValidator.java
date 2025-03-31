package com.demo.user.profile.validator.impl;

import com.demo.user.profile.validator.UserAuthValidator;
import com.demo.user.profile.validator.UserValidatorPojo;
import org.springframework.stereotype.Component;

@Component("blockDeviceValidator")
public class BlockDeviceValidator implements UserAuthValidator {

    @Override
    public int getRunningOrder() {
        return 2;
    }

    @Override
    public <T extends UserValidatorPojo> void validate(T request) {
        // TODO impl
    }
}
