
package com.jkuo.sample.controller;

import com.jkuo.sample.service.RateLimitService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.jkuo.sample.config.RedisConfig;

//@SpringBootTest

@SpringJUnitConfig
@ContextConfiguration(classes = { RedisConfig.class, RateLimitService.class })
@ActiveProfiles("local")
@TestPropertySource("classpath:application-local.properties")
public class RateLimitControllerIT {

    static Process redisProcess;

    @BeforeAll
    static void startRedis() throws IOException {
        redisProcess = new ProcessBuilder("redis-server").start();
        System.out.println("Redis started for tests...");
    }

    @AfterAll
    static void stopRedis() {
        try {
            Runtime.getRuntime().exec("redis-cli FLUSHALL");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (redisProcess != null) {
            redisProcess.destroy();
            System.out.println("Redis stopped after tests...");
        }
    }

    @Autowired
    private RateLimitService rateLimitService;

    @Test
    public void testRateLimit() throws Exception {

        // arrange
        String clientIp = "1.1.1.1";
        // act
        boolean permitSuccess = rateLimitService.getRateLimiter(clientIp).tryAcquire(3);
        boolean permitFail = rateLimitService.getRateLimiter(clientIp).tryAcquire(1);
        // assert
        assertTrue(permitSuccess);
        assertFalse(permitFail);

    }

}