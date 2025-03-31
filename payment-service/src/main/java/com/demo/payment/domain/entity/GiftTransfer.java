package com.demo.payment.domain.entity;

import com.demo.payment.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "gift_transfers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftTransfer extends BaseEntity{

    @Id
    private String id;

    @Column(name = "transaction_id",  nullable = false, unique = true)
    private String transactionId;

    @Column(name = "cif",  nullable = false)
    private String cif;

    @Column(name = "sender_account_no",  nullable = false)
    private String senderAccountNo;

    @Column(name = "gift_code", nullable = false, unique = true)
    private String giftCode;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "remarks")
    private String remarks;

}
