package com.demo.payment.service;

import com.demo.payment.dto.request.GiftClaimRequestDTO;
import com.demo.payment.dto.request.GiftTransferRequestDTO;
import com.demo.payment.dto.response.GiftTransferResponseDTO;
import com.demo.payment.dto.response.PaymentConfirmResponseDTO;

public interface GiftTransferService {
    GiftTransferResponseDTO createGift(GiftTransferRequestDTO request);
    PaymentConfirmResponseDTO claimGift(GiftClaimRequestDTO request);
}
