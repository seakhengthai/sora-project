package com.demo.gateway.service.impl;

import com.demo.gateway.domain.entity.API;
import com.demo.gateway.exception.AppException;
import com.demo.gateway.repository.APIDefRepository;
import com.demo.gateway.service.APIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.demo.gateway.exception.code.AppErrorCode.API_NOT_FOUND;


@Service
@Slf4j
@RefreshScope
public class APIServiceImpl implements APIService {

    @Autowired
    private APIDefRepository apiDefRepository;

    @Override
    @Cacheable(value = "apidefs")
    public List<API> findAll() {
        log.info("Find all api defs");
        return StreamSupport.stream(apiDefRepository.findAll().spliterator(),
                false)
                .collect(Collectors.toList());
    }
}
