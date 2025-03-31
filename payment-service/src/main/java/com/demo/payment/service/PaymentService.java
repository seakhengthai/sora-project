package com.demo.payment.service;

import com.demo.payment.domain.entity.Service;
import com.demo.payment.domain.entity.TxOrder;
import com.demo.payment.dto.request.PaymentRequestDTO;
import com.demo.payment.dto.response.PaymentConfirmResponseDTO;
import com.demo.payment.dto.response.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO create(PaymentRequestDTO request, Service service);
    PaymentConfirmResponseDTO confirm(TxOrder txOrder);
}
