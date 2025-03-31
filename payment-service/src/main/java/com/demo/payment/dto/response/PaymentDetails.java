package com.demo.payment.dto.response;

import com.demo.payment.domain.OrderCommand;
import com.demo.payment.domain.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
    OrderCommand command;
    PaymentStatus status;
    PaymentUserResponse sender;
    PaymentUserResponse receiver;
    BigDecimal amount;
    String currency;
    CrossCurrencyResponse crossCurrency;
    Double fee;
    Long feeTierId;
    String purpose;
    String remarks;
    ServiceResponse service;
    Map<String, String> additionalRef;
}
