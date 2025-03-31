package com.demo.user.profile.domain.entity;


import com.demo.user.profile.domain.DrcrInd;
import com.demo.user.profile.domain.TxnType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_transactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"external_reference", "account_no"})
})
public class AccountTransaction extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "external_reference")
    private String externalReference;

    @Column(name = "cif")
    private String cif;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "drcr_ind")
    @Enumerated(EnumType.STRING)
    private DrcrInd drcrInd;

    @Column(name = "txn_type")
    @Enumerated(EnumType.STRING)
    private TxnType txnType;

    @Column(name = "ending_balance", precision = 19, scale = 2)
    private BigDecimal endingBalance;

    public String getDescription(){
        return txnType.getMessage() + "\n(" + referenceNumber + ")";
    }

    public String getTxnDate(){
        return new SimpleDateFormat("dd MMM yyyy | HH:mm").format(this.getCreatedAt());
    }
}
