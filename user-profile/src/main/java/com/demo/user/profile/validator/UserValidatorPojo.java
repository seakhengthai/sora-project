package com.demo.user.profile.validator;

import com.demo.user.profile.domain.VerifyType;
import com.demo.user.profile.domain.entity.UserAuthDetail;
import com.demo.user.profile.dto.request.AuthRequestDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserValidatorPojo {
    @Builder.Default
    VerifyType verifyType = VerifyType.ON_LOGIN;
    String cif;
    String deviceId;
    AuthRequestDTO authRequest;
    UserAuthDetail authDetails;
}
