package com.jkuo.sample.service;

import org.redisson.api.RedissonClient;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {
    private final RedissonClient redissonClient;

    @Autowired
    public RateLimitService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RRateLimiter getRateLimiter(String clientIp) {
        String key = "rate-limiter:" + clientIp; 
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);

        if (!rateLimiter.isExists()) {
            rateLimiter.trySetRate(RateType.OVERALL, 3, 1, RateIntervalUnit.MINUTES);
            redissonClient.getBucket(key).expire(1, TimeUnit.MINUTES); 
        }
        return rateLimiter;
    }
}