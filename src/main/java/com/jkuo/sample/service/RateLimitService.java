package com.jkuo.sample.service;

import org.redisson.api.RedissonClient;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

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
        rateLimiter.trySetRate(RateType.OVERALL, 3, Duration.ofMinutes(1));
        redissonClient.getBucket(key).expire(Duration.ofMinutes(2)); 
        return rateLimiter;
    }
}