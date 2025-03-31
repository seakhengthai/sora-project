package com.demo.user.profile.controller;

import com.demo.user.profile.dto.request.AccountInquiryRequestDTO;
import com.demo.user.profile.dto.request.FundTransferRequestDTO;
import com.demo.user.profile.dto.response.APIResponse;
import com.demo.user.profile.service.AccountService;
import com.demo.user.profile.service.FundTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final FundTransferService fundTransferService;

    @GetMapping
    public APIResponse<?> getAccounts(@RequestHeader("cif") String cif) {
        return APIResponse.success(accountService.getAccountByCif(cif));
    }

    @PostMapping("/inquiry")
    public APIResponse<?> accountInquiry(@RequestBody @Valid AccountInquiryRequestDTO request) {
        return APIResponse.success(accountService.accountInquiry(request.getAccountNo()));
    }

    @PostMapping("/transfer")
    public APIResponse<?> transferFunds(@RequestBody @Valid FundTransferRequestDTO request) {
        fundTransferService.transferFunds(request);
        return APIResponse.success();
    }

    @GetMapping("/internal/account/{accountNo}")
    public APIResponse<?> getAccountByAccount(@PathVariable("accountNo") String accountNo) {
        return APIResponse.success(accountService.getAccountByAccountNoAndActive(accountNo));
    }

    @GetMapping("/internal/cif/{cif}/account/{accountNo}")
    public APIResponse<?> getAccountByCifAndAccount(@PathVariable("cif") String cif,
                                              @PathVariable("accountNo") String accountNo) {
        return APIResponse.success(accountService.getAccountByCifAndAccountNoAndActive(cif, accountNo));
    }
}

