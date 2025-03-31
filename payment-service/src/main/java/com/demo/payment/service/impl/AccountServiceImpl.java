package com.demo.payment.service.impl;

import com.demo.payment.config.fiegn.UserProfileFeignClient;
import com.demo.payment.domain.PaymentUserType;
import com.demo.payment.domain.UserAccount;
import com.demo.payment.domain.entity.PaymentUser;
import com.demo.payment.domain.entity.TxOrder;
import com.demo.payment.dto.request.FundTransferRequestDTO;
import com.demo.payment.service.AccountService;
import com.demo.payment.utils.EquivalentAmountUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserProfileFeignClient userProfileFeignClient;

    @Override
    public UserAccount getAccount(String accountNo) {
        return userProfileFeignClient.getAccountByAccountNo(accountNo).getData();
    }

    @Override
    public UserAccount getAccount(String cif, String accountNo) {
        return userProfileFeignClient.getAccountByCifAndAccountNo(cif, accountNo).getData();
    }

    @Override
    public void transferFunds(TxOrder txOrder) {
        PaymentUser sender = txOrder.getPaymentUser(PaymentUserType.SENDER);
        PaymentUser receiver = txOrder.getPaymentUser(PaymentUserType.RECEIVER);

        FundTransferRequestDTO fundTransfer = FundTransferRequestDTO.builder()
                .externalReference(txOrder.getTransactionId())
                .txnType(txOrder.getService().getTxnType())
                .senderAccountNo(sender.getAccountNumber())
                .receiverAccountNo(receiver.getAccountNumber())
                .build();

        String fromCcy = sender.getAccountCcy();
        String toCcy = receiver.getAccountCcy();
        String txnCcy = txOrder.getCurrency();

        BigDecimal debitAmount = txOrder.getAmount();
        BigDecimal creditAmount = txOrder.getAmount();

        if(EquivalentAmountUtils.isCrossCurrency(fromCcy, toCcy, txnCcy)){
            if(EquivalentAmountUtils.isCrossSender(fromCcy, txnCcy)){
                debitAmount = txOrder.getEquivalentAmount();
            }else {
                creditAmount = txOrder.getEquivalentAmount();
            }
        }
        fundTransfer.setDebitAmount(debitAmount);
        fundTransfer.setCreditAmount(creditAmount);
        Map<String, String> header = Map.of("cif", sender.getCif());
        userProfileFeignClient.transferFunds(header, fundTransfer);
    }
}
