package com.demo.gateway.repository;

import com.demo.gateway.domain.entity.ClientPublishAPI;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientPublishAPIRepository extends CrudRepository<ClientPublishAPI,Long> {
    List<ClientPublishAPI> findAll();
}
