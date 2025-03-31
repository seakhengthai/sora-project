package com.demo.gateway.repository;

import com.demo.gateway.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends JpaRepository<Client,String>, JpaSpecificationExecutor<Client> {
}
