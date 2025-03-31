package com.demo.payment.dto.response;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftTransferResponseDTO {
	String transactionId;
	String giftCode;
	Date expiresAt;
}
