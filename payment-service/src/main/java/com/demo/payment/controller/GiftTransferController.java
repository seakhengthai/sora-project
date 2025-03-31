package com.demo.payment.controller;

import com.demo.payment.dto.request.GiftClaimRequestDTO;
import com.demo.payment.dto.request.GiftTransferRequestDTO;
import com.demo.payment.dto.response.APIResponse;
import com.demo.payment.service.GiftTransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/{$version:v1.0}/gift-transfer")
@AllArgsConstructor
public class GiftTransferController {

    private final GiftTransferService giftTransferService;

    @PostMapping("/create")
    public APIResponse<?> createGift(@RequestBody @Valid GiftTransferRequestDTO request) {
        return APIResponse.success(giftTransferService.createGift(request));
    }

    @PostMapping("/claim")
    public APIResponse<?> claimGift(@RequestBody @Valid GiftClaimRequestDTO request) {
        return APIResponse.success(giftTransferService.claimGift(request));
    }
}
