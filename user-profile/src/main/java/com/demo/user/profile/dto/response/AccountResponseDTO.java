package com.demo.user.profile.dto.response;

import com.demo.user.profile.domain.entity.UserAccount;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountResponseDTO {
    String cif;
    String accountNo;
    String currency;
    BigDecimal balance;
    String accountName;
    Boolean isHide;
    String description;
    Boolean isDefault;

    public static AccountResponseDTO buildFromEntity(UserAccount account){
        return AccountResponseDTO.builder()
                .cif(account.getCif())
                .accountNo(account.getAccountNo())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .accountName(account.getAccountName())
                .isHide(account.getIsHide())
                .description(account.getDescription())
                .isDefault(account.getIsDefault())
                .build();
    }
}
