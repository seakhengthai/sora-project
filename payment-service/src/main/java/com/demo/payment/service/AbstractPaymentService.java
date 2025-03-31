package com.demo.payment.service;

import com.demo.payment.domain.PaymentStep;
import com.demo.payment.domain.entity.Service;
import com.demo.payment.dto.request.PaymentRequestDTO;
import com.demo.payment.logger.LogAround;
import com.demo.payment.validator.PaymentValidatePojo;
import com.demo.payment.validator.PaymentValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public abstract class AbstractPaymentService {

    protected final ApplicationContext applicationContext;

    @LogAround(message = "Payment Validator")
    public void validatorPayment(PaymentValidatePojo paymentValidatePojo, PaymentStep orderStep) {
        String[] paymentValidatorsName = applicationContext.getBeanNamesForType(PaymentValidator.class);
        List<PaymentValidator> validatorBeans = Arrays.stream(paymentValidatorsName)
                .map(beanName -> applicationContext.getBean(beanName, PaymentValidator.class))
                .sorted(Comparator.comparingInt(PaymentValidator::getRunningOrder))
                .toList();
        log.debug("Payment validation classes [{}] to validate", validatorBeans);
        validatorBeans.stream()
                .filter(validatorBean -> validatorBean.shouldValidate(paymentValidatePojo))
                .filter(validatorBean -> validatorBean.validateStep().equals(orderStep) ||
                        validatorBean.validateStep().equals(PaymentStep.CREATE_AND_EXECUTE))
                .forEach(paymentValidator -> paymentValidator.validateOrder(paymentValidatePojo));
    }

    public String transactionId(PaymentRequestDTO request, Service service) {
        // TODO generate with prefix base service
        return UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 16)
                .toLowerCase();
    }
}
