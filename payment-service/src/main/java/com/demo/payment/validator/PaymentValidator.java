package com.demo.payment.validator;

import com.demo.payment.domain.PaymentStep;

public interface PaymentValidator {

    boolean validateOrder(PaymentValidatePojo paymentValidatePojo);

    PaymentStep validateStep();

    default int getRunningOrder() {
        return 100;
    }

    default boolean shouldValidate(PaymentValidatePojo paymentValidatePojo) {
        return true;
    }
}
