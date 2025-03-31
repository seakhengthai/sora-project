package com.demo.user.profile.controller;

import com.demo.user.profile.dto.request.AccountStatementRequestDTO;
import com.demo.user.profile.dto.request.StatementDownloadRequestDTO;
import com.demo.user.profile.dto.response.APIResponse;
import com.demo.user.profile.service.AccountTransactionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequestMapping("/api/v1.0/accounts/statement")
@RequiredArgsConstructor
@Slf4j
public class AccountStatementController {

    private final AccountTransactionService accountTransactionService;

    @PostMapping
    public APIResponse<?> accountStatement(@RequestHeader("cif") String cif, @RequestBody @Valid AccountStatementRequestDTO model) {
        model.setCif(cif);
        return APIResponse.success(accountTransactionService.getAccountStatements(model));
    }

    @PostMapping("/download")
    public void statementDownload(HttpServletResponse response, @RequestHeader("cif") String cif,
                                  @RequestBody @Valid StatementDownloadRequestDTO request) throws ParseException {
        request.setCif(cif);
        accountTransactionService.statementDownload(response, request);
    }
}

