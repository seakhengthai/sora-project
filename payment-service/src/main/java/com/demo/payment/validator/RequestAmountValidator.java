package com.demo.payment.validator;

import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import com.demo.payment.utils.EquivalentAmountUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("requestAmountValidator")
public class RequestAmountValidator {

    public void validate(PaymentValidatePojo paymentValidatePojo) {
        BigDecimal amountToCheck = paymentValidatePojo.getPaymentRequest().getAmount();
        if(EquivalentAmountUtils.isCrossSender(paymentValidatePojo.getSender().getCurrency(),
                paymentValidatePojo.getPaymentRequest().getCurrency())){
            amountToCheck = paymentValidatePojo.getPaymentRequest().getEquivalentAmount();
        }
        if(paymentValidatePojo.getSender().getBalance().compareTo(amountToCheck) < 0){
            throw new ApplicationException(AppErrorCode.INSUFFICIENT_FUN);
        }
    }

}
