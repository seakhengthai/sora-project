package com.demo.payment.service;


import com.demo.payment.domain.PaymentUserType;
import com.demo.payment.domain.entity.PaymentUser;
import com.demo.payment.domain.entity.Service;
import com.demo.payment.domain.entity.TxOrder;
import com.demo.payment.dto.request.ConfirmRequestDTO;
import com.demo.payment.dto.request.PaymentRequestDTO;
import com.demo.payment.dto.response.PaymentConfirmResponseDTO;
import com.demo.payment.dto.response.PaymentResponseDTO;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import com.demo.payment.logger.LogAround;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@org.springframework.stereotype.Service("paymentServiceFactory")
@AllArgsConstructor
@Slf4j
public class PaymentServiceFactory {

    private final ServiceService serviceService;
    private final PaymentService paymentService;
    private final TxOrderService txOrderService;

    @LogAround(message = "Payment Create")
    public PaymentResponseDTO createPayment(PaymentRequestDTO request){
        Service service = serviceService.findByServiceIdAndCurrencyAndActive(
                request.getServiceId(), request.getCurrency());
        return paymentService.create(request, service);
    }

    @LogAround(message = "Payment Confirm")
    @Transactional
    public PaymentConfirmResponseDTO confirmPayment(ConfirmRequestDTO request) {
        log.info("Start to execute payment with id [{}]", request.getId());
        TxOrder txOrder = txOrderService.findByIdAndStatusCreateThenLocked(request.getId());
        Optional<PaymentUser> optSender = txOrder.getPaymentUsers().stream()
                .filter(i->PaymentUserType.SENDER.equals(i.getUserType()))
                .filter(i->i.getCif().equals(request.getCif()))
                .findFirst();
        if(optSender.isEmpty()){
            throw new ApplicationException(AppErrorCode.PAYMENT_USER_INVALID);
        }
        return paymentService.confirm(txOrder);
    }
}
