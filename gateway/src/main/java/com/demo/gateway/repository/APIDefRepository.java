package com.demo.gateway.repository;

import com.demo.gateway.domain.entity.API;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface APIDefRepository extends CrudRepository<API, Long>, JpaSpecificationExecutor<API> {
    Optional<API> findByCode(String aLong);
    List<API> findAllByCodeIn(List<String> codes);
}
