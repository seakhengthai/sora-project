package com.demo.payment.validator;

import com.demo.payment.domain.UserAccount;
import com.demo.payment.domain.entity.Service;
import com.demo.payment.domain.entity.TxOrder;
import com.demo.payment.dto.request.PaymentRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public final class PaymentValidatePojo {
    PaymentRequestDTO paymentRequest;
    Service service;
    UserAccount sender;
    UserAccount receiver;
    TxOrder txOrder;
}
