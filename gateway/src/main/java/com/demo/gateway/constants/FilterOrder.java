package com.demo.gateway.constants;

import org.springframework.core.Ordered;

public class FilterOrder {
    // Smallest is highest
    public static int GLOBAL_CLIENT_FILTER = Ordered.HIGHEST_PRECEDENCE;
    public static int SECURITY_HEADER_ORDER = -1000;
    public static int API_AUTHORIZATION_ORDER = -100;
    public static int LDAP_AUTHORIZATION_ORDER = -101;
    public static int REWRITE_TOKEN_ORDER = -10;
    public static int WEB_FILTER_ORDER = -9;
    public static int POLICY_FILTER = 100;
    public static int PAYMENT_PIN_FILTER = 1000;
    public static int LOGGER_FILTER = 10000000;

}
