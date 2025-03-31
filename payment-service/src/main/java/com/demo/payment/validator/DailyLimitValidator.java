package com.demo.payment.validator;

import com.demo.payment.domain.PaymentStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("dailyLimitValidator")
@RequiredArgsConstructor
public class DailyLimitValidator implements PaymentValidator {

    @Override
    public int getRunningOrder() {
        return 5;
    }

    @Override
    public boolean validateOrder(PaymentValidatePojo paymentValidatePojo) {
        // TODO impl
        return true;
    }

    @Override
    public PaymentStep validateStep() {
        return PaymentStep.CREATE_AND_EXECUTE;
    }

}


