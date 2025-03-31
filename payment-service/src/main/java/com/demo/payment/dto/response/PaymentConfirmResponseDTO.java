package com.demo.payment.dto.response;

import com.demo.payment.domain.PaymentStatus;
import lombok.*;

import java.io.Serializable;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmResponseDTO implements Serializable {
    PaymentStatus status;
    String message;
    String transactionId;
}
