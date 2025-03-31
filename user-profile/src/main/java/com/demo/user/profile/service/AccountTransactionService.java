package com.demo.user.profile.service;

import com.demo.user.profile.domain.DrcrInd;
import com.demo.user.profile.domain.entity.AccountTransaction;
import com.demo.user.profile.domain.entity.UserAccount;
import com.demo.user.profile.dto.request.AccountStatementRequestDTO;
import com.demo.user.profile.dto.request.FundTransferRequestDTO;
import com.demo.user.profile.dto.request.StatementDownloadRequestDTO;
import com.demo.user.profile.dto.response.PaginationResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;

public interface AccountTransactionService {
    AccountTransaction save(UserAccount account, FundTransferRequestDTO fundTransferRequest, DrcrInd drcrInd);
    PaginationResponseDTO<?> getAccountStatements(AccountStatementRequestDTO request);
    void statementDownload(HttpServletResponse httpServletResponse, StatementDownloadRequestDTO request) throws ParseException;
}
