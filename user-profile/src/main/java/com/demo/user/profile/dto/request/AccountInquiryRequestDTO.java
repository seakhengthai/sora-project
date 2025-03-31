package com.demo.user.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInquiryRequestDTO {
    @NotBlank
    String accountNo;
}