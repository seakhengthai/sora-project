package com.demo.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GiftTransferRequestDTO {
    @NotBlank
    String senderAccountNo;
    @NotNull
    BigDecimal amount;
    @NotBlank
    String currency;
    String purpose;
    String remarks;
}
