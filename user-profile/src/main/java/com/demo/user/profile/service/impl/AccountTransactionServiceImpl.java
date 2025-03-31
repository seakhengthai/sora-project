package com.demo.user.profile.service.impl;

import com.demo.user.profile.domain.DrcrInd;
import com.demo.user.profile.domain.FileType;
import com.demo.user.profile.domain.entity.AccountTransaction;
import com.demo.user.profile.domain.entity.UserAccount;
import com.demo.user.profile.dto.request.AccountStatementRequestDTO;
import com.demo.user.profile.dto.request.FundTransferRequestDTO;
import com.demo.user.profile.dto.request.StatementDownloadRequestDTO;
import com.demo.user.profile.dto.response.AccountStatementResponseDTO;
import com.demo.user.profile.dto.response.PageResponse;
import com.demo.user.profile.dto.response.PaginationResponseDTO;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.exception.code.AppErrorCode;
import com.demo.user.profile.export.ExportAccountStatementExcel;
import com.demo.user.profile.export.ExportAccountStatementPdf;
import com.demo.user.profile.repository.AccountTransactionRepository;
import com.demo.user.profile.service.AccountService;
import com.demo.user.profile.service.AccountTransactionService;
import com.demo.user.profile.utils.TimePeriod;
import com.demo.user.profile.utils.TimePeriodUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountTransactionServiceImpl implements AccountTransactionService {

    private final AccountTransactionRepository accountTransactionRepository;
    private final AccountService accountService;
    private final ExportAccountStatementExcel exportAccountStatementExcel;
    private final ExportAccountStatementPdf exportAccountStatementPdf;

    @Override
    public AccountTransaction save(UserAccount account, FundTransferRequestDTO fundTransferRequest, DrcrInd drcrInd) {
        AccountTransaction accountTransaction = AccountTransaction.builder()
                .referenceNumber(fundTransferRequest.getReferenceNumber())
                .externalReference(fundTransferRequest.getExternalReference())
                .cif(account.getCif())
                .accountNo(account.getAccountNo())
                .currency(account.getCurrency())
                .amount(DrcrInd.CREDIT.equals(drcrInd)
                        ? fundTransferRequest.getCreditAmount()
                        : fundTransferRequest.getDebitAmount())
                .drcrInd(drcrInd)
                .txnType(fundTransferRequest.getTxnType())
                .drcrInd(drcrInd)
                .endingBalance(account.getBalance())
                .build();
        return accountTransactionRepository.save(accountTransaction);
    }

    @Override
    public PaginationResponseDTO<?> getAccountStatements(AccountStatementRequestDTO request) {
        Sort sort =  Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
        Page<AccountTransaction> page = accountTransactionRepository.findByCifAndAccountNo(request.getCif(),
                request.getAccountNo(), pageable);
        List<AccountStatementResponseDTO> transactions = page.getContent()
                .stream().map(AccountStatementResponseDTO::buildFromEntity)
                .toList();
        PageResponse pageResponse = PageResponse.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
        return new PaginationResponseDTO<>(pageResponse, transactions);
    }

    @Override
    public void statementDownload(HttpServletResponse httpServletResponse, StatementDownloadRequestDTO request) throws ParseException {
        UserAccount userAccount = accountService.findByCifAndAccountNoAndActive(request.getCif(), request.getAccountNo());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        TimePeriod statementPeriod = TimePeriodUtils.getPeriod(request);
        List<AccountTransaction> accountTransactions = accountTransactionRepository
                .findAllByCifAndAccountNoAndCreatedAtBetweenOrderByCreatedAtDesc(request.getCif(), request.getAccountNo(),
                        formatter.parse(statementPeriod.getFromDate()), formatter.parse(statementPeriod.getToDate()));
        if (accountTransactions.isEmpty()) throw new ApplicationException(AppErrorCode.ACCOUNT_STATEMENT_EMPTY);
        if(FileType.PDF.equals(request.getFileType())){
            exportAccountStatementPdf.createPdfReport(httpServletResponse, request, userAccount, accountTransactions);
        } else if (FileType.EXCEL.equals(request.getFileType())) {
            exportAccountStatementExcel.export(httpServletResponse, userAccount, request , accountTransactions);
        } else throw new RuntimeException("No impl!");

    }

}
