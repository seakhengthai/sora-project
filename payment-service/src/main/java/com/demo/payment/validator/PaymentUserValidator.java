package com.demo.payment.validator;

import com.demo.payment.domain.UserAccount;
import com.demo.payment.domain.entity.Service;
import com.demo.payment.dto.request.PaymentRequestDTO;
import com.demo.payment.service.AccountService;
import com.demo.payment.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Setter
public class PaymentUserValidator {

    private final AccountService accountService;

    public UserAccount validateOnSender(String senderAccountNo){
        return accountService.getAccount(AppUtils.getCif(), senderAccountNo);
    }

    public UserAccount validateOnSender(PaymentRequestDTO request, Service service){
        UserAccount account = accountService.getAccount(AppUtils.getCif(), request.getSenderAccountNo());
        // TODO validate on account: account_class...
        return account;
    }

    public UserAccount validateOnReceiver(PaymentRequestDTO request, Service service){
        UserAccount account = accountService.getAccount(request.getReceiverAccountNo());
        // TODO validate on account: account_class...
        return account;
    }
}
