package com.demo.user.profile.repository;

import com.demo.user.profile.domain.entity.AccountTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long>{
    Page<AccountTransaction> findByCifAndAccountNo(String cif, String accountNo, Pageable pageable);
    List<AccountTransaction> findAllByCifAndAccountNoAndCreatedAtBetweenOrderByCreatedAtDesc(String cif, String accountNo, Date fromDate, Date toDate);
}
