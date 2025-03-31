package com.demo.payment.controller;

import com.demo.payment.dto.request.ConfirmRequestDTO;
import com.demo.payment.dto.request.PaymentRequestDTO;
import com.demo.payment.dto.response.APIResponse;
import com.demo.payment.service.PaymentServiceFactory;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{$version:v1.0}/payments")
@AllArgsConstructor
public class PaymentOrderController {

    private final PaymentServiceFactory paymentServiceFactory;

    @PostMapping("/create")
    public APIResponse<?> createPayment(@RequestBody @Valid PaymentRequestDTO request) {
        return APIResponse.success(paymentServiceFactory.createPayment(request));
    }

    @PostMapping("/confirm")
    public APIResponse<?> confirmPayment(@RequestHeader("cif") String cif,
                                         @RequestBody @Valid ConfirmRequestDTO request) {
        request.setCif(cif);
        return APIResponse.success(paymentServiceFactory.confirmPayment(request));
    }
}
