package com.demo.user.profile.repository;

import com.demo.user.profile.domain.entity.UserAuthDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthDetailRepository extends JpaRepository<UserAuthDetail, Long>{
    Optional<UserAuthDetail> findFirstByCif(String cif);
    Optional<UserAuthDetail> findFirstByMobileNumber(String mobileNumber);

}
