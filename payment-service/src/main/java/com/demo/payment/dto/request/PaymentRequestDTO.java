package com.demo.payment.dto.request;

import com.demo.payment.domain.OrderCommand;
import com.demo.payment.domain.PaymentUserType;
import com.demo.payment.domain.entity.PaymentUser;
import com.demo.payment.domain.entity.TxOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO{
    String transactionId;
    BigDecimal equivalentAmount;
    Double exchangeRate;
    @NotBlank(message = "Service Id must be not null")
    String serviceId;
    @Builder.Default
    OrderCommand command = OrderCommand.PAYMENT;
    @NotBlank(message = "Sender account must be not null")
    String senderAccountNo;
    @NotBlank(message = "Receiver account must be not null")
    String receiverAccountNo;
    @NotNull(message = "Amount must be not null.")
    BigDecimal amount;
    @NotNull(message = "Currency must be not null")
    String currency;
    @NotNull(message = "Purpose must be not null.")
    String purpose;
    String remarks;
    String channel;
    Map<String, String> additionalRef;

    public static PaymentRequestDTO build(TxOrder txOrder){
        PaymentUser sender = txOrder.getPaymentUser(PaymentUserType.SENDER);
        PaymentUser receiver = txOrder.getPaymentUser(PaymentUserType.RECEIVER);
        return PaymentRequestDTO.builder()
                .transactionId(txOrder.getTransactionId())
                .equivalentAmount(txOrder.getEquivalentAmount())
                .exchangeRate(txOrder.getExchangeRate())
                .serviceId(txOrder.getService().getServiceId())
                .senderAccountNo(sender.getAccountNumber())
                .receiverAccountNo(receiver.getAccountNumber())
                .amount(txOrder.getAmount())
                .currency(txOrder.getCurrency())
                .purpose(txOrder.getPurpose())
                .remarks(txOrder.getRemarks())
                .channel(txOrder.getChannel())
                .additionalRef(txOrder.getAdditionalRef())
                .build();
    }
}
