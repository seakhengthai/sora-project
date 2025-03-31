package com.demo.gateway.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@Setter
@AllArgsConstructor
public class PaymentAuthorizeRequestDTO {
    private String tpin;
    private String cif;
    private String deviceId;
    private String appVersion;
    private String location;
    private String language;
}
