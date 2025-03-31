package com.demo.payment.validator;

import com.demo.payment.domain.PaymentStep;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("amountValidator")
@RequiredArgsConstructor
public class AmountValidator implements PaymentValidator {

    @Override
    public int getRunningOrder() {
        return 1;
    }

    @Override
    public boolean validateOrder(PaymentValidatePojo paymentValidatePojo) {
        BigDecimal amount = paymentValidatePojo.getPaymentRequest().getAmount();
        String currency = paymentValidatePojo.getPaymentRequest().getCurrency();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApplicationException(AppErrorCode.AMOUNT_GT_ZERO);
        }
        if("KHR".equals(currency)
                && amount.remainder(new BigDecimal("50")).compareTo(BigDecimal.ZERO) != 0){
            throw new ApplicationException(AppErrorCode.PAYMENT_AMOUNT_INVALID);
        }
        return true;
    }

    @Override
    public PaymentStep validateStep() {
        return PaymentStep.CREATE;
    }

}


