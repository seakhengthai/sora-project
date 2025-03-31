package com.demo.payment.domain.entity;

import com.demo.payment.domain.OrderCommand;
import com.demo.payment.domain.PaymentStatus;
import com.demo.payment.domain.PaymentUserType;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tx_orders")
public class TxOrder extends BaseEntity {

    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "order_command")
    @Enumerated(EnumType.STRING)
    private OrderCommand orderCommand;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "equivalent_amount", precision = 18, scale = 2)
    private BigDecimal equivalentAmount;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_tier_id")
    private FeeTier feeTier;

    @Column(name = "fee")
    private Double fee;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "channel")
    private String channel;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "txOrder", fetch = FetchType.EAGER)
    private List<PaymentUser> paymentUsers;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "tx_order_additional_ref", joinColumns =
    @JoinColumn(name = "tx_order_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> additionalRef;

    public PaymentUser getPaymentUser(PaymentUserType type){
        return paymentUsers.stream()
                .filter(i-> type.equals(i.getUserType()))
                .findFirst()
                .orElseThrow(()->new ApplicationException(AppErrorCode.PAYMENT_USER_INVALID));
    }

}
