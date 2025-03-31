package com.demo.user.profile.domain;

import lombok.Getter;

@Getter
public enum TxnType {

    OWN("OWN", "Transfer Own Account"),
    WITHIN("WITHIN", "Account Transfer"),
    GIFT("GIFT", "Gift");

    final String code;
    final String message;

    TxnType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
