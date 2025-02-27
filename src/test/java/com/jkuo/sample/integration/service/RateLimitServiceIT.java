
package com.jkuo.sample.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import com.jkuo.sample.config.RedisConfig;
import com.jkuo.sample.service.RateLimitService;

@Tag("integration")
@SpringJUnitConfig
@ContextConfiguration(classes = { RedisConfig.class, RateLimitService.class })
@ActiveProfiles("local")
public class RateLimitServiceIT {

    public static final DockerImageName REDIS_IMAGE = DockerImageName
            .parse("redis:latest");

    @SuppressWarnings("resource")
    static GenericContainer<?> redis = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(6379);

    @BeforeAll
    static void beforeAll() {

        try {
            redis.start();
            Integer redisPort = redis.getMappedPort(6379);
            System.setProperty("spring.redis.host", "localhost");
            System.setProperty("spring.redis.port", redisPort.toString());
            System.err.println("Redis container started.");
        } catch (Exception e) {
            System.err.println("Failed to start Redis container: " + e.getMessage());
            throw e;
        }
    }

    @AfterAll
    static void afterAll() {
        if (redis.isRunning()) {
            redis.stop();
            System.err.println("Redis container stopped.");
        }
    }

    @Autowired
    private RateLimitService rateLimitService;

    @Test
    public void testPermitAcquire() {
        // arrange
        String clientIp = "1.1.1.1";
        // act
        boolean permitSuccess = rateLimitService.getRateLimiter(clientIp).tryAcquire(3);
        boolean permitFail = rateLimitService.getRateLimiter(clientIp).tryAcquire(1);
        // assert
        assertTrue(permitSuccess);
        assertFalse(permitFail);
    }

    @Test
    public void testTimeToLive() {
        // arrange
        String clientIp = "2.2.2.2";
        // act
        rateLimitService.getRateLimiter(clientIp).tryAcquire(1);
        // assert
        long ttl = rateLimitService.getTimeToLive(clientIp);
        assertTrue(ttl > 0, "TTL should be set and greater than zero");
    }

}