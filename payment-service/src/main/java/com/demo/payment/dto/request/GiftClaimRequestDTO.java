package com.demo.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftClaimRequestDTO {
    @NotBlank
    String giftCode;
    @NotBlank
    String receiverAccountNo;
}
