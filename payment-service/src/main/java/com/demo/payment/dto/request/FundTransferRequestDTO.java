package com.demo.payment.dto.request;

import com.demo.payment.domain.TxnType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundTransferRequestDTO {
    String externalReference;
    TxnType txnType;
    String senderAccountNo;
    String receiverAccountNo;
    BigDecimal debitAmount;
    BigDecimal creditAmount;
}