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

    private int rateLimit = 3;
    private Duration rateLimitDuration = Duration.ofMinutes(1);
    private Duration rateLimitExpiration = Duration.ofMinutes(2);

    @Autowired
    public RateLimitService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RateLimitService(RedissonClient redissonClient, int rateLimit, Duration rateLimitDuration,
            Duration rateLimitExpiration) {
        this.redissonClient = redissonClient;
        this.rateLimit = rateLimit;
        this.rateLimitDuration = rateLimitDuration;
        this.rateLimitExpiration = rateLimitExpiration;
    }

    public RRateLimiter getRateLimiter(String clientIp) {
        String key = "rate-limiter:" + clientIp;
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, rateLimit, rateLimitDuration);
        redissonClient.getBucket(key).expire(rateLimitExpiration);
        return rateLimiter;
    }
}