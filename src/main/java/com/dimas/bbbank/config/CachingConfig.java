package com.dimas.bbbank.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dimas.bbbank.util.Util.CACHE_NAME_EMAILS;
import static com.dimas.bbbank.util.Util.CACHE_NAME_PHONES;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_NAME_EMAILS, CACHE_NAME_PHONES);
    }
}