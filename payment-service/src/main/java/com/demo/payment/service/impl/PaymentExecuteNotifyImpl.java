package com.demo.payment.service.impl;

import com.demo.payment.domain.entity.TxOrder;
import com.demo.payment.service.PaymentExecuteNotify;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentExecuteNotifyImpl implements PaymentExecuteNotify {
    @Override
    public void onSuccess(TxOrder txOrder) {
        // TODO push notification
        // TODO push webhook to client
    }

    @Override
    public void onFailure(TxOrder txOrder, Throwable throwable) {

    }
}
