package com.demo.gateway.filter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class PaymentAuthorizeConfig {

    private List<String> paths;
    private String requiredLength;

}
