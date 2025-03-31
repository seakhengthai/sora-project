package com.demo.user.profile.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
public class UserPinVerifyResponseDTO {
    boolean valid;
}
