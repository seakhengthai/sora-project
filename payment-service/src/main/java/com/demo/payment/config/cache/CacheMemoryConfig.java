package com.demo.payment.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(value = "caching.type",
        havingValue = "memory")
public class CacheMemoryConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheMemoryConfig.class);
    private final CacheConfiguration cacheConfiguration;

    @Autowired
    public CacheMemoryConfig(
            @Name("cacheConfigurationSpecs") CacheConfiguration cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }

    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        SimpleCacheManager manager = new SimpleCacheManager();
        Map<String, CacheConfiguration.CacheSpec> specs = cacheConfiguration.getSpecs();
        if (specs != null) {
            List<CaffeineCache> caches =
                    specs.entrySet().stream()
                            .map(entry -> buildCache(entry.getKey(), entry.getValue(), ticker))
                            .collect(Collectors.toList());
            manager.setCaches(caches);
        }
        return manager;
    }

    private CaffeineCache buildCache(String name, CacheConfiguration.CacheSpec cacheSpec, Ticker ticker) {
        LOGGER.info("Cache {} specified expired of {} second max size of {}.", name, cacheSpec.getExpired(),
                cacheSpec.getMax());
        final Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(cacheSpec.getExpired(), TimeUnit.SECONDS)
                .maximumSize(cacheSpec.getMax())
                .ticker(ticker);
        return new CaffeineCache(name, caffeineBuilder.build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

}
