package com.demo.user.profile.service.impl;

import com.demo.user.profile.domain.DrcrInd;
import com.demo.user.profile.domain.entity.UserAccount;
import com.demo.user.profile.dto.request.FundTransferRequestDTO;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.exception.code.AppErrorCode;
import com.demo.user.profile.service.AccountService;
import com.demo.user.profile.service.AccountTransactionService;
import com.demo.user.profile.service.FundTransferService;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FundTransferServiceImpl implements FundTransferService {

    private final AccountService accountService;
    private final AccountTransactionService accountTransactionService;

    @Override
    @Transactional
    public void transferFunds(FundTransferRequestDTO request) {
        UserAccount sender = accountService.findByAccountNoAndActiveThenLocked(request.getSenderAccountNo());
        UserAccount receiver = accountService.findByAccountNoAndActive(request.getReceiverAccountNo());

        if(sender.getAccountNo().equals(receiver.getAccountNo()))
            throw new ApplicationException(AppErrorCode.SAME_ACCOUNT);

        if(!sender.getCurrency().equals(receiver.getCurrency())
                && request.getDebitAmount().compareTo(request.getCreditAmount()) == 0)
            throw new ApplicationException(AppErrorCode.INVALID_AMOUNT);

        if (sender.getBalance().compareTo(request.getDebitAmount()) < 0)
            throw new ApplicationException(AppErrorCode.INSUFFICIENT_BALANCE);

        // Generate unique reference number
        String referenceNumber = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 16)
                .toLowerCase();
        request.setReferenceNumber(referenceNumber);

        // Perform fund transfer (debit sender, credit receiver)
        try{
            accountTransactionService.save(sender, request, DrcrInd.DEBIT);
        }catch (DataIntegrityViolationException ex){
            if (ex.getCause() instanceof ConstraintViolationException sqlException) {
                if ("23505".equals(sqlException.getSQLState())) {
                    throw new ApplicationException(AppErrorCode.DUPLICATE_EXT_REF);
                }
            }
            throw ex;
        }
        sender.setBalance(sender.getBalance().subtract(request.getDebitAmount()));
        accountService.save(sender);

        accountTransactionService.save(receiver, request, DrcrInd.CREDIT);
        receiver.setBalance(receiver.getBalance().add(request.getCreditAmount()));
        accountService.save(receiver);
    }
}
