package com.demo.gateway.repository;

import com.demo.gateway.domain.Status;
import com.demo.gateway.domain.entity.ClientTokenLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientTokenLogRepository extends CrudRepository<ClientTokenLog, Long> {
    Optional<ClientTokenLog> findByTokenIdAndStatusAndDeviceId(String tokenId, Status status, String deviceId);
    Optional<ClientTokenLog> findByCifAndStatus(String cif, Status status);
}
