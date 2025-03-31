package com.demo.payment.service.impl;

import com.demo.payment.domain.PaymentStatus;
import com.demo.payment.domain.PaymentUserType;
import com.demo.payment.domain.SaveModel;
import com.demo.payment.domain.entity.PaymentUser;
import com.demo.payment.domain.entity.TxOrder;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.repository.TxOrderRepository;
import com.demo.payment.service.TxOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static com.demo.payment.exception.code.AppErrorCode.PAYMENT_ID_INVALID;

@Service
@AllArgsConstructor
public class TxOrderServiceImpl implements TxOrderService {

    private final TxOrderRepository txOrderRepository;

    @Override
    public TxOrder findByIdAndStatusCreateThenLocked(String id) {
        return txOrderRepository.findByIdAndPaymentStatus(id, PaymentStatus.CREATED)
                .orElseThrow(()->new ApplicationException(PAYMENT_ID_INVALID));
    }

    @Override
    public TxOrder save(SaveModel model) {
        TxOrder txOrder = TxOrder.builder()
                .id( UUID.randomUUID().toString())
                .transactionId(model.getPaymentRequest().getTransactionId())
                .service(model.getService())
                .orderCommand(model.getPaymentRequest().getCommand())
                .amount(model.getPaymentRequest().getAmount())
                .currency(model.getPaymentRequest().getCurrency())
                .additionalRef(model.getPaymentRequest().getAdditionalRef())
                .purpose(model.getPaymentRequest().getPurpose())
                .remarks(model.getPaymentRequest().getRemarks())
                .equivalentAmount(model.getPaymentRequest().getEquivalentAmount())
                .exchangeRate(model.getPaymentRequest().getExchangeRate())
                .channel(model.getPaymentRequest().getChannel())
                .build();
        txOrder.setPaymentStatus(PaymentStatus.CREATED);

        var paymentUsers = new ArrayList<PaymentUser>();
        paymentUsers.add(PaymentUser.builder()
                .txOrder(txOrder)
                .cif(model.getSender().getCif())
                .accountNumber(model.getSender().getAccountNo())
                .accountCcy(model.getSender().getCurrency())
                .userType(PaymentUserType.SENDER)
                .build());
        paymentUsers.add(PaymentUser.builder()
                .txOrder(txOrder)
                .cif(model.getReceiver().getCif())
                .accountNumber(model.getReceiver().getAccountNo())
                .accountCcy(model.getReceiver().getCurrency())
                .userType(PaymentUserType.RECEIVER)
                .build());
        txOrder.setPaymentUsers(paymentUsers);
        return txOrderRepository.save(txOrder);
    }

    @Override
    public TxOrder save(TxOrder txOrder) {
        return txOrderRepository.save(txOrder);
    }

    @Override
    public void updatePaymentStatus(TxOrder txOrder, PaymentStatus status) {
        txOrder.setPaymentStatus(status);
        txOrderRepository.save(txOrder);
    }

}
