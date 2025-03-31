package com.demo.payment.config.cache;


import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Map;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

@Configuration
@ConditionalOnProperty(value = "redis.caching.enabled", havingValue = "true")
public class CacheRedisConfig {

    private Logger LOG = LoggerFactory.getLogger(CacheRedisConfig.class);

    @Autowired
    @Qualifier("cacheConfigurationSpecs")
    private CacheConfiguration cacheConfiguration;

    @Value(value = "${spring.cache.redis.key-prefix:}")
    private String springCacheRedisKeyPrefix;

    @Value("${spring.cache.redis.use-key-prefix:true}")
    private boolean isEnablePrefixCache;

    @Autowired
    private Environment environment;

    private transient CacheKeyPrefix cacheKeyPrefix;

    @PostConstruct
    private void onPostConstruct() {
        if (springCacheRedisKeyPrefix != null) {
            springCacheRedisKeyPrefix = springCacheRedisKeyPrefix.trim();
        } else {
            String[] activeProfiles = environment.getActiveProfiles();
            springCacheRedisKeyPrefix = activeProfiles[0].trim();
        }
        if (isEnablePrefixCache) {
            cacheKeyPrefix = cacheName -> springCacheRedisKeyPrefix + "::" + cacheName + "::";
        } else {
            cacheKeyPrefix = CacheKeyPrefix.simple();
        }
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            Map<String, CacheConfiguration.CacheSpec> specs = cacheConfiguration.getSpecs();
            if (specs != null) {
                specs.entrySet()
                        .stream()
                        .peek(c -> LOG.info("Cache prefix:{} with key:{} and values:{}",cacheKeyPrefix, c.getKey(), c.getValue()))
                        .forEach(spec -> builder
                                .withCacheConfiguration(
                                        spec.getKey(),
                                        defaultCacheConfig()
                                                .computePrefixWith(cacheKeyPrefix)
                                                .entryTtl(Duration.ofSeconds(spec.getValue().getExpired()))
                                )
                        );
            }
        };
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
