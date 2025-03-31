package com.demo.payment.dto.response;

import com.demo.payment.domain.PaymentUserType;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrossCurrencyResponse {
	PaymentUserType type;
	BigDecimal equivalentAmount;
	String exchangeCurrency;
	Double exchangeRate;
}
