package com.demo.gateway.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration("cacheConfigurationSpecs")
@ConfigurationProperties(prefix = "gw.cache")
@Data
public class CacheConfiguration {

    private Map<String, CacheSpec> specs;

    @Data
    public static class CacheSpec {

        private Integer expired=300;
        private Integer max = 50;
    }


}
