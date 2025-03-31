package com.demo.user.profile.controller;

import com.demo.user.profile.dto.request.AuthRequestDTO;
import com.demo.user.profile.dto.request.UserPinVerifyRequestDTO;
import com.demo.user.profile.dto.response.APIResponse;
import com.demo.user.profile.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/account/verify")
    public APIResponse<?> authentication(@RequestBody @Valid AuthRequestDTO request) {
        return APIResponse.success(authenticationService.authentication(request));
    }

    @PostMapping("/accounts/authorize")
    public APIResponse<?> paymentAuthorize(@RequestBody UserPinVerifyRequestDTO request) {
        return APIResponse.success(authenticationService.paymentAuthorize(request));
    }
}

