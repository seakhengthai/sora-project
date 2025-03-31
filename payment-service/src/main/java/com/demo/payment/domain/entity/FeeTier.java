package com.demo.payment.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fee_tiers")
public class FeeTier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "from_amount")
    private Double fromAmount;

    @Column(name = "to_amount")
    private Double toAmount;

    @Column(name = "fee")
    private Double fee;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "min")
    private Double min;

    @Column(name = "max")
    private Double max;

    @Column(name = "type", length = 20)
    private String feeType;


    public Double getFeeAmount(Double txnAmount) {
        if ("FIXED".equals(feeType))
            return this.getFee();
        else if ("RATE".equals(feeType)) {
            double amountRate = txnAmount * rate / 100;
            double minRate = Objects.nonNull(min) ? min : 0.0;
            double maxRate = Objects.nonNull(max) ? max : 0.0;
            if (amountRate <= minRate)
                return minRate;
            if (maxRate > 0.0 && amountRate >= max) return max;

            // Standard rounding
            if ("KHR".equals(currency)) {
                amountRate = (double) (Math.round(amountRate / 100) * 100);
            } else {
                amountRate = Math.round(amountRate * 100.0) / 100.0;
            }
            return amountRate;
        }
        return 0.0;
    }
}
