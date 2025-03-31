package com.demo.payment.service;


import com.demo.payment.domain.entity.TxOrder;

public interface PaymentExecuteNotify {

    void onSuccess(TxOrder txOrder);

    void onFailure(TxOrder txOrder, Throwable throwable);

}
