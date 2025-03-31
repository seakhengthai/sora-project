package com.demo.user.profile.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountInquiryResponseDTO {
    String name;
    String accountNo;
    String currency;
}
