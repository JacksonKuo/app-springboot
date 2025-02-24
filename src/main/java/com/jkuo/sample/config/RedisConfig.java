package com.jkuo.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        try {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
            return Redisson.create(config);
        } catch (Exception e) {
            return null;
        }
    }
}