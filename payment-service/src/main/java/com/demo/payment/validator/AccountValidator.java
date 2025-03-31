package com.demo.payment.validator;

import com.demo.payment.domain.PaymentStep;
import com.demo.payment.domain.UserAccount;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("accountValidator")
@RequiredArgsConstructor
public class AccountValidator implements PaymentValidator {

    @Override
    public int getRunningOrder() {
        return 2;
    }

    @Override
    public boolean validateOrder(PaymentValidatePojo paymentValidatePojo) {
        UserAccount sender = paymentValidatePojo.getSender();
        UserAccount receiver = paymentValidatePojo.getReceiver();
        if(sender.getAccountNo().equals(receiver.getAccountNo())) {
            throw new ApplicationException(AppErrorCode.SAME_ACCOUNT);
        }
        if(!List.of(sender.getCurrency(), receiver.getCurrency())
                .contains(paymentValidatePojo.getPaymentRequest().getCurrency())){
            throw new ApplicationException(AppErrorCode.PAYMENT_CURRENCY_INVALID);
        }

        return true;
    }

    @Override
    public PaymentStep validateStep() {
        return PaymentStep.CREATE;
    }

}


