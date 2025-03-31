package com.demo.user.profile.repository;

import com.demo.user.profile.domain.Status;
import com.demo.user.profile.domain.entity.UserAccount;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{
    List<UserAccount> findAllByCif(String cif);

    Optional<UserAccount> findFirstByAccountNo(String accountNo);
    Optional<UserAccount> findFirstByCifAndAccountNo(String cif, String accountNo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "2000")})
    Optional<UserAccount> findFirstByAccountNoAndStatus(String accountNo, Status status);
}
