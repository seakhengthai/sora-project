package com.demo.payment.service.impl;

import com.demo.payment.domain.PaymentStatus;
import com.demo.payment.domain.UserAccount;
import com.demo.payment.domain.entity.GiftTransfer;
import com.demo.payment.dto.request.ConfirmRequestDTO;
import com.demo.payment.dto.request.GiftClaimRequestDTO;
import com.demo.payment.dto.request.GiftTransferRequestDTO;
import com.demo.payment.dto.request.PaymentRequestDTO;
import com.demo.payment.dto.response.GiftTransferResponseDTO;
import com.demo.payment.dto.response.PaymentConfirmResponseDTO;
import com.demo.payment.dto.response.PaymentResponseDTO;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.repository.GiftTransferRepository;
import com.demo.payment.service.GiftTransferService;
import com.demo.payment.service.PaymentServiceFactory;
import com.demo.payment.validator.AmountValidator;
import com.demo.payment.validator.CurrencyValidator;
import com.demo.payment.validator.PaymentUserValidator;
import com.demo.payment.validator.PaymentValidatePojo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.demo.payment.exception.code.AppErrorCode.GIFT_CODE_EXPIRED;
import static com.demo.payment.exception.code.AppErrorCode.GIFT_CODE_INVALID;

@Service
@RequiredArgsConstructor
@RefreshScope
public class GiftTransferServiceImpl implements GiftTransferService {

    private final static String SERVICE_ID = "transfer_gift";

    @Value("${gift-code.expired.minute:1440}")
    private int expiredMinute;

    private final GiftTransferRepository giftTransferRepository;
    private final PaymentUserValidator paymentUserValidator;
    private final AmountValidator amountValidator;
    private final CurrencyValidator currencyValidator;
    private final PaymentServiceFactory paymentServiceFactory;

    @Override
    public GiftTransferResponseDTO createGift(GiftTransferRequestDTO request) {
        UserAccount senderAccount = paymentUserValidator.validateOnSender(request.getSenderAccountNo());
        PaymentValidatePojo paymentValidatePojo = PaymentValidatePojo.builder()
                .paymentRequest(PaymentRequestDTO.builder()
                        .senderAccountNo(request.getSenderAccountNo())
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .build())
                .sender(senderAccount)
                .build();
        currencyValidator.validateOrder(paymentValidatePojo);
        amountValidator.validateOrder(paymentValidatePojo);

        String transactionId = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 16)
                .toLowerCase();
        String rawGiftCode = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
        GiftTransfer giftTransfer = GiftTransfer.builder()
                .id(UUID.randomUUID().toString())
                .transactionId(transactionId)
                .cif(senderAccount.getCif())
                .senderAccountNo(request.getSenderAccountNo())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .giftCode(rawGiftCode)
                .paymentStatus(PaymentStatus.CREATED)
                .purpose(request.getPurpose())
                .remarks(request.getRemarks())
                .build();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiredMinute);
        giftTransfer.setExpiresAt(calendar.getTime());
        giftTransferRepository.save(giftTransfer);
        return GiftTransferResponseDTO.builder()
                .transactionId(giftTransfer.getTransactionId())
                .giftCode(rawGiftCode)
                .expiresAt(giftTransfer.getExpiresAt())
                .build();
    }

    @Override
    @Transactional
    public PaymentConfirmResponseDTO claimGift(GiftClaimRequestDTO request) {
        GiftTransfer giftTransfer = giftTransferRepository
                .findByGiftCodeAndPaymentStatus(request.getGiftCode(), PaymentStatus.CREATED)
                .orElseThrow(()->new ApplicationException(GIFT_CODE_INVALID));
        if(giftTransfer.getExpiresAt().before(new Date())){
            throw new ApplicationException(GIFT_CODE_EXPIRED);
        }
        giftTransfer.setPaymentStatus(PaymentStatus.CLAIMED);
        giftTransferRepository.save(giftTransfer);

        PaymentRequestDTO paymentRequest = PaymentRequestDTO.builder()
                .transactionId(giftTransfer.getTransactionId())
                .serviceId(SERVICE_ID)
                .senderAccountNo(giftTransfer.getSenderAccountNo())
                .receiverAccountNo(request.getReceiverAccountNo())
                .amount(giftTransfer.getAmount())
                .currency(giftTransfer.getCurrency())
                .purpose(giftTransfer.getPurpose())
                .remarks(giftTransfer.getRemarks())
                .channel("INTERNAL")
                .additionalRef(Map.of("gift_ref_id", giftTransfer.getId()))
                .build();
        PaymentResponseDTO paymentResponse = paymentServiceFactory.createPayment(paymentRequest);
        ConfirmRequestDTO confirmRequest = ConfirmRequestDTO.builder()
                .cif(giftTransfer.getCif())
                .id(paymentResponse.getId())
                .build();
        return paymentServiceFactory.confirmPayment(confirmRequest);
    }

}
