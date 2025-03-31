package com.demo.payment.validator;

import com.demo.payment.domain.PaymentStep;
import com.demo.payment.domain.entity.TxOrder;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Component("paymentExpiredValidator")
@RefreshScope
public class PaymentExpiredValidator implements PaymentValidator {

    @Value("${payment.expired.second:300}")
    private int paymentExpiredSecond;

    @Override
    public int getRunningOrder() {
        return 1;
    }

    @Override
    public boolean validateOrder(PaymentValidatePojo paymentValidatePojo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paymentValidatePojo.getTxOrder().getCreatedAt());
        calendar.add(Calendar.SECOND, paymentExpiredSecond);
        if (new Date().compareTo(calendar.getTime()) > 0) {
            throw new ApplicationException(AppErrorCode.PAYMENT_CONFIRM_EXPIRED);
        }
        return true;
    }

    @Override
    public PaymentStep validateStep() {
        return PaymentStep.EXECUTE;
    }

}


