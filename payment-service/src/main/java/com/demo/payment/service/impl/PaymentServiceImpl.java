package com.demo.payment.service.impl;

import com.demo.payment.domain.PaymentStatus;
import com.demo.payment.domain.PaymentStep;
import com.demo.payment.domain.SaveModel;
import com.demo.payment.domain.UserAccount;
import com.demo.payment.domain.entity.FeeTier;
import com.demo.payment.domain.entity.TxOrder;
import com.demo.payment.dto.request.PaymentRequestDTO;
import com.demo.payment.dto.response.PaymentConfirmResponseDTO;
import com.demo.payment.dto.response.PaymentResponseDTO;
import com.demo.payment.service.AbstractPaymentService;
import com.demo.payment.service.InMemoryPaymentQueue;
import com.demo.payment.service.PaymentService;
import com.demo.payment.service.TxOrderService;
import com.demo.payment.utils.EquivalentAmountUtils;
import com.demo.payment.validator.PaymentUserValidator;
import com.demo.payment.validator.PaymentValidatePojo;
import com.demo.payment.validator.RequestAmountValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;

@Service
@Slf4j
public class PaymentServiceImpl extends AbstractPaymentService implements PaymentService {

    private final PaymentUserValidator paymentUserValidator;
    private final RequestAmountValidator requestAmountValidator;
    private final TxOrderService txOrderService;
    private final InMemoryPaymentQueue paymentQueue;

    public PaymentServiceImpl(ApplicationContext applicationContext,
                              PaymentUserValidator paymentUserValidator,
                              RequestAmountValidator requestAmountValidator,
                              TxOrderService txOrderService,
                              InMemoryPaymentQueue paymentQueue) {
        super(applicationContext);
        this.paymentUserValidator = paymentUserValidator;
        this.requestAmountValidator = requestAmountValidator;
        this.txOrderService = txOrderService;
        this.paymentQueue = paymentQueue;
    }

    @Override
    public PaymentResponseDTO create(PaymentRequestDTO request, com.demo.payment.domain.entity.Service service) {
        UserAccount senderAccount = paymentUserValidator.validateOnSender(request, service);
        UserAccount receiverAccount = paymentUserValidator.validateOnReceiver(request, service);

        String fromCcy = senderAccount.getCurrency();
        String toCcy = receiverAccount.getCurrency();
        String txnCcy = request.getCurrency();
        BigDecimal amount = request.getAmount();

        PaymentValidatePojo paymentValidatePojo = PaymentValidatePojo.builder()
                .paymentRequest(request)
                .service(service)
                .sender(senderAccount)
                .receiver(receiverAccount)
                .build();
        super.validatorPayment(paymentValidatePojo, PaymentStep.CREATE);

        if(StringUtils.isEmpty(request.getTransactionId())) {
            request.setTransactionId(super.transactionId(request, service));
        }
        request.setEquivalentAmount(amount);
        request.setExchangeRate(1.0);

        if(EquivalentAmountUtils.isCrossCurrency(fromCcy, toCcy, txnCcy)){
            Tuple2<BigDecimal, Double> equivalentAmount = EquivalentAmountUtils.getEquivalentAmount(amount,
                    fromCcy, toCcy, txnCcy);
            request.setEquivalentAmount(equivalentAmount.getT1());
            request.setExchangeRate(equivalentAmount.getT2());
        }
        requestAmountValidator.validate(paymentValidatePojo);

        FeeTier feeTier = null; //TODO get transaction fee
        SaveModel saveModel = SaveModel.builder()
                .paymentRequest(request)
                .service(service)
                .sender(senderAccount)
                .receiver(receiverAccount)
                .build();
        TxOrder txOrder = txOrderService.save(saveModel);
        return PaymentResponseDTO.buildPaymentResponse(txOrder);
    }

    @Override
    public PaymentConfirmResponseDTO confirm(TxOrder txOrder) {
        PaymentRequestDTO paymentRequest = PaymentRequestDTO.build(txOrder);
        UserAccount senderAccount = paymentUserValidator.validateOnSender(paymentRequest, txOrder.getService());
        PaymentValidatePojo paymentValidatePojo = PaymentValidatePojo.builder()
                .paymentRequest(paymentRequest)
                .service(txOrder.getService())
                .sender(senderAccount)
                .txOrder(txOrder)
                .build();
        super.validatorPayment(paymentValidatePojo, PaymentStep.EXECUTE);

        // Save transaction
        txOrderService.updatePaymentStatus(txOrder, PaymentStatus.PENDING);

        // Add to processing queue
        paymentQueue.enqueue(txOrder);

        return PaymentConfirmResponseDTO.builder()
                .status(PaymentStatus.PENDING)
                .message("Payment accepted and queued for processing")
                .transactionId(txOrder.getTransactionId())
                .build();
    }
}
