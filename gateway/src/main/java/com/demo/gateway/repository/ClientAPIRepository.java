package com.demo.gateway.repository;

import com.demo.gateway.domain.entity.ClientAPI;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientAPIRepository extends CrudRepository<ClientAPI, Long> {
    @Override
    Optional<ClientAPI> findById(Long aLong);
}
