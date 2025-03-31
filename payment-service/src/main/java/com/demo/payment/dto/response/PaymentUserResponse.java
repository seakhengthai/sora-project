package com.demo.payment.dto.response;

import com.demo.payment.domain.PaymentUserType;
import com.demo.payment.domain.entity.PaymentUser;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUserResponse {
	String accountNo;
	String currency;

	public static PaymentUserResponse build(List<PaymentUser> paymentUsers, PaymentUserType type){
		return paymentUsers.stream()
			.filter(i-> type.equals(i.getUserType()))
			.findAny().map(PaymentUserResponse::buildResponse)
				.orElseThrow(()->new ApplicationException(AppErrorCode.PAYMENT_USER_INVALID));
	}

	public static PaymentUserResponse buildResponse(PaymentUser paymentUser){
		return PaymentUserResponse.builder()
				.accountNo(paymentUser.getAccountNumber())
				.currency(paymentUser.getAccountCcy())
				.build();
	}
}
