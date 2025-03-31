package com.demo.payment.repository;

import com.demo.payment.domain.Status;
import com.demo.payment.domain.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, String>{
    Optional<Service> findFirstByServiceIdAndCurrencyAndStatus(String serviceId, String currency, Status status);
}
