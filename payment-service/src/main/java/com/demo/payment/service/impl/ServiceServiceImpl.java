package com.demo.payment.service.impl;

import com.demo.payment.domain.Status;
import com.demo.payment.domain.entity.Service;
import com.demo.payment.exception.ApplicationException;
import com.demo.payment.exception.code.AppErrorCode;
import com.demo.payment.repository.ServiceRepository;
import com.demo.payment.service.ServiceService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Override
    @Cacheable(value = "services", key = "{#serviceId, #currency}")
    public Service findByServiceIdAndCurrencyAndActive(String serviceId, String currency) {
        return serviceRepository.findFirstByServiceIdAndCurrencyAndStatus(serviceId, currency, Status.ACTIVE)
                .orElseThrow(()-> new ApplicationException(AppErrorCode.SERVICE_NOT_FOUND));
    }
}
