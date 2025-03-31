package com.demo.user.profile.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_accounts")
public class UserAccount extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "cif")
    private String cif;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "is_hide")
    private Boolean isHide;

    @Column(name = "description")
    private String description;

    @Column(name = "is_default")
    private Boolean isDefault;
}
