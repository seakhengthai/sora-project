package com.demo.user.profile.dto.response;

import com.demo.user.profile.domain.DrcrInd;
import com.demo.user.profile.domain.TxnType;
import com.demo.user.profile.domain.entity.AccountTransaction;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementResponseDTO {
    Long id;
    String referenceNumber;
    String externalReference;
    String accountNo;
    String currency;
    BigDecimal amount;
    DrcrInd drcrInd;
    TxnType txnType;
    Date txnDate;

    public static AccountStatementResponseDTO buildFromEntity(AccountTransaction txn){
        return AccountStatementResponseDTO.builder()
                .id(txn.getId())
                .referenceNumber(txn.getReferenceNumber())
                .externalReference(txn.getExternalReference())
                .accountNo(txn.getAccountNo())
                .currency(txn.getCurrency())
                .amount(txn.getAmount())
                .drcrInd(txn.getDrcrInd())
                .txnType(txn.getTxnType())
                .txnDate(txn.getCreatedAt())
                .build();
    }
}