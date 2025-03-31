package com.demo.user.profile.service.impl;

import com.demo.user.profile.domain.GrantType;
import com.demo.user.profile.domain.entity.UserAuthDetail;
import com.demo.user.profile.dto.request.AuthRequestDTO;
import com.demo.user.profile.dto.request.UserPinVerifyRequestDTO;
import com.demo.user.profile.dto.response.UserPinVerifyResponseDTO;
import com.demo.user.profile.dto.response.UserProfileResponseDTO;
import com.demo.user.profile.dto.response.UserVerifyResponseDTO;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.service.AbstractAuthentication;
import com.demo.user.profile.service.AuthenticationService;
import com.demo.user.profile.service.UserAuthDetailService;
import com.demo.user.profile.utils.AppUtils;
import com.demo.user.profile.utils.CryptographyUtils;
import com.demo.user.profile.validator.UserValidatorPojo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.demo.user.profile.exception.code.AppErrorCode.CREDENTIAL_NOT_VALID;
import static com.demo.user.profile.exception.code.AppErrorCode.INVALID_GRANT_TYPE;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl extends AbstractAuthentication
        implements AuthenticationService {

    private final UserAuthDetailService userAuthDetailService;

    @Override
    public UserVerifyResponseDTO authentication(AuthRequestDTO request) {
        UserAuthDetail userAuth = userAuthDetailService.findByMobileNumber(request.getUserName());

        super.userLoginValidator(UserValidatorPojo.builder()
                        .cif(userAuth.getCif())
                        .deviceId(AppUtils.getDeviceId())
                        .authDetails(userAuth).build());

        if (GrantType.password.equals(request.getGrantType())) {
            super.validatePIN(request.getPassword(), userAuth);
        } else if (GrantType.refresh_token.equals(request.getGrantType())) {
            request.setRelogin(true);
        } else throw new ApplicationException(INVALID_GRANT_TYPE);
        // TODO login via biometric

        userAuth.setLastLoginDate(new Date());
        userAuthDetailService.save(userAuth);

        return UserVerifyResponseDTO.builder()
                .verifiedId(CryptographyUtils.randomStr())
                .cif(userAuth.getCif())
                .userProfile(UserProfileResponseDTO.buildFromUserAuth(userAuth))
                .build();
    }

    @Override
    public UserPinVerifyResponseDTO paymentAuthorize(UserPinVerifyRequestDTO request) {
        String cif = AppUtils.getCif();
        if(StringUtils.isEmpty(request.getTpin())){
            throw new ApplicationException(CREDENTIAL_NOT_VALID);
        }

        UserAuthDetail userAuth = userAuthDetailService.findByCif(cif);
        super.userLoginValidator(UserValidatorPojo.builder()
                .cif(userAuth.getCif())
                .deviceId(AppUtils.getDeviceId())
                .authDetails(userAuth)
                .build());

        super.validatePIN(request.getTpin(), userAuth);
        return UserPinVerifyResponseDTO
                .builder()
                .valid(true)
                .build();
    }

}
