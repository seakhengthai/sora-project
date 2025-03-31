package com.demo.payment.repository;

import com.demo.payment.domain.entity.PaymentUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentUserRepository extends JpaRepository<PaymentUser, Long>{
}
