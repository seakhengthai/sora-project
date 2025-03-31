package com.demo.payment.service;

import com.demo.payment.domain.UserAccount;
import com.demo.payment.domain.entity.TxOrder;

public interface AccountService {
    UserAccount getAccount(String accountNo);
    UserAccount getAccount(String cif, String accountNo);
    void transferFunds(TxOrder txOrder);
}
