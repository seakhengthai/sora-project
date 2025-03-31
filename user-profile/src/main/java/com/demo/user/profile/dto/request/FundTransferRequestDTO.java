package com.demo.user.profile.dto.request;

import com.demo.user.profile.domain.TxnType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundTransferRequestDTO {
    String referenceNumber;
    @NotBlank
    String externalReference;
    @NotNull
    TxnType txnType;
    @NotBlank
    String senderAccountNo;
    @NotBlank
    String receiverAccountNo;
    @NotNull
    BigDecimal debitAmount;
    @NotNull
    BigDecimal creditAmount;
}