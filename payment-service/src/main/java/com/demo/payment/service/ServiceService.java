package com.demo.payment.service;

import com.demo.payment.domain.entity.Service;

public interface ServiceService {
    Service findByServiceIdAndCurrencyAndActive(String serviceId, String currency);
}
