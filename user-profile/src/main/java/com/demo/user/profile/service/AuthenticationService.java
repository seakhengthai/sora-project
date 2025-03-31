package com.demo.user.profile.service;

import com.demo.user.profile.dto.request.AuthRequestDTO;
import com.demo.user.profile.dto.request.UserPinVerifyRequestDTO;
import com.demo.user.profile.dto.response.UserPinVerifyResponseDTO;
import com.demo.user.profile.dto.response.UserVerifyResponseDTO;

public interface AuthenticationService {
    UserVerifyResponseDTO authentication(AuthRequestDTO request);
    UserPinVerifyResponseDTO paymentAuthorize(UserPinVerifyRequestDTO request);
}
