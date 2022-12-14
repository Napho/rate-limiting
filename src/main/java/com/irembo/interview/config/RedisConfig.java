package com.irembo.interview.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;

@Configuration
public class RedisConfig {

    @Value("${redis.servers}")
    private String redisServer;

    @Bean
    public Config config() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisServer);
        return config;
    }

    @Bean
    public CacheManager cacheManager(Config config) {
        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cacheManager.createCache("cache", RedissonConfiguration.fromConfig(config));
        return cacheManager;
    }

    @Bean
    ProxyManager<String> proxyManager(CacheManager cacheManager) {
        return new JCacheProxyManager<>(cacheManager.getCache("cache"));
    }

}
