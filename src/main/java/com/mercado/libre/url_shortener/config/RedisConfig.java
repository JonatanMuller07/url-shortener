package com.mercado.libre.url_shortener.config;

import com.mercado.libre.url_shortener.util.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig {

    @Value(value = "${cache.redis.address}")
    private String serverAddress;

    @Bean
    public RedissonClient redissonClient() {
        var config = new Config();
        config.useSingleServer()
                .setAddress(serverAddress);
        return Redisson.create(config);
    }

    @Bean
    public Jedis jedis() {
        return new Jedis(serverAddress);
    }

    @Bean
    @Autowired
    public CacheManager cacheManager(RedissonClient redissonClient) {
        var configs = Map.of(
                CacheConstants.URL_CACHE_NAME, new CacheConfig()
        );
        return new RedissonSpringCacheManager(redissonClient, configs);
    }
}
