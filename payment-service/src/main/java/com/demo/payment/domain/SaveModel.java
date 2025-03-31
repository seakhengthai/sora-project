package com.demo.payment.domain;

import com.demo.payment.domain.entity.Service;
import com.demo.payment.dto.request.PaymentRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SaveModel {
    PaymentRequestDTO paymentRequest;
    Service service;
    UserAccount sender;
    UserAccount receiver;
}
