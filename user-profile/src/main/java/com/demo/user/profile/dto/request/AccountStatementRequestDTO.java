package com.demo.user.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementRequestDTO {
    @Builder.Default
    int pageNumber = 0;
    @Builder.Default
    int pageSize = 20;
    String cif;
    @NotBlank
    String accountNo;
}