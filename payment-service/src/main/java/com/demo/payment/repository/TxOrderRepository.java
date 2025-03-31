package com.demo.payment.repository;

import com.demo.payment.domain.PaymentStatus;
import com.demo.payment.domain.entity.TxOrder;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface TxOrderRepository extends JpaRepository<TxOrder, String>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "2000")})
    Optional<TxOrder> findByIdAndPaymentStatus(String id, PaymentStatus status);
}
