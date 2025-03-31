package com.demo.payment.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAccount {
    String cif;
    String accountNo;
    String currency;
    BigDecimal balance;
    String accountName;
}
