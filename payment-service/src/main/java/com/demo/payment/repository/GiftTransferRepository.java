package com.demo.payment.repository;

import com.demo.payment.domain.PaymentStatus;
import com.demo.payment.domain.entity.GiftTransfer;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface GiftTransferRepository extends JpaRepository<GiftTransfer, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "2000")})
    Optional<GiftTransfer> findByGiftCodeAndPaymentStatus(String giftCode, PaymentStatus status);
}
