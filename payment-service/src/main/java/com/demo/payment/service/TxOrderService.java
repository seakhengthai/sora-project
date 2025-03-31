package com.demo.payment.service;

import com.demo.payment.domain.PaymentStatus;
import com.demo.payment.domain.SaveModel;
import com.demo.payment.domain.entity.TxOrder;

public interface TxOrderService {
    TxOrder findByIdAndStatusCreateThenLocked(String id);
    TxOrder save(SaveModel model);
    TxOrder save(TxOrder txOrder);
    void updatePaymentStatus(TxOrder txOrder, PaymentStatus status);
}
