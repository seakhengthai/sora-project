package com.demo.payment.validator;

import com.demo.payment.domain.Currency;
import com.demo.payment.domain.PaymentStep;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("currencyValidator")
@AllArgsConstructor
@Slf4j
public class CurrencyValidator implements PaymentValidator {

    @Override
    public boolean validateOrder(PaymentValidatePojo paymentValidatePojo) {
        Optional<Currency> findCurrency = Arrays
                .stream(Currency.values())
                .filter(currency -> paymentValidatePojo.getPaymentRequest().getCurrency()
                        .equals(currency.name()))
                .findFirst();
        if (findCurrency.isEmpty()) {
            throw new ApplicationException(AppErrorCode.PAYMENT_CURRENCY_INVALID);
        }

        return true;
    }

    @Override
    public PaymentStep validateStep() {
        return PaymentStep.CREATE;
    }

    @Override
    public int getRunningOrder() {
        return 1;
    }
}
