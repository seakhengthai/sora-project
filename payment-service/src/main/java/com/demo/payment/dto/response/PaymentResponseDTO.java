package com.demo.payment.dto.response;

import com.demo.payment.domain.PaymentUserType;
import com.demo.payment.domain.entity.TxOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO implements Serializable {
    String transactionId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Date transactionDate;
    String id;
    PaymentDetails paymentDetails;
    String channel;

    public static PaymentResponseDTO buildPaymentResponse(TxOrder txOrder){
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .command(txOrder.getOrderCommand())
                .status(txOrder.getPaymentStatus())
                .service(ServiceResponse.builder()
                        .id(txOrder.getService().getId())
                        .name(txOrder.getService().getName())
                        .serviceId(txOrder.getService().getServiceId())
                        .build())
                .sender(PaymentUserResponse.build(txOrder.getPaymentUsers(), PaymentUserType.SENDER))
                .receiver(PaymentUserResponse.build(txOrder.getPaymentUsers(), PaymentUserType.RECEIVER))
                .amount(txOrder.getAmount())
                .currency(txOrder.getCurrency())
                .fee(0.0)
                .purpose(txOrder.getPurpose())
                .remarks(txOrder.getRemarks())
                .additionalRef(txOrder.getAdditionalRef())
                .build();
        paymentDetails.setCrossCurrency(buildCrossCurrencyResponse(txOrder, paymentDetails));

        return PaymentResponseDTO.builder()
                .transactionId(txOrder.getTransactionId())
                .transactionDate(txOrder.getCreatedAt())
                .id(txOrder.getId())
                .paymentDetails(paymentDetails)
                .channel(txOrder.getChannel())
                .build();
    }

    public static CrossCurrencyResponse buildCrossCurrencyResponse(TxOrder txOrder, PaymentDetails paymentDetails){
        if(!paymentDetails.getSender().getCurrency().equals(txOrder.getCurrency())){
            return CrossCurrencyResponse.builder()
                    .type(PaymentUserType.SENDER)
                    .equivalentAmount(txOrder.getEquivalentAmount())
                    .exchangeRate(txOrder.getExchangeRate())
                    .exchangeCurrency(paymentDetails.getSender().getCurrency())
                    .build();
        } else if (!paymentDetails.getReceiver().getCurrency().equals(txOrder.getCurrency())) {
            return CrossCurrencyResponse.builder()
                    .type(PaymentUserType.RECEIVER)
                    .equivalentAmount(txOrder.getEquivalentAmount())
                    .exchangeRate(txOrder.getExchangeRate())
                    .exchangeCurrency(paymentDetails.getReceiver().getCurrency())
                    .build();
        }
        return null;
    }
}
