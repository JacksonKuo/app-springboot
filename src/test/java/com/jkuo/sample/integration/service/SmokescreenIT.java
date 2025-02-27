package com.jkuo.sample.integration.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.boot.test.context.SpringBootTest;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import reactor.core.publisher.Mono;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.jkuo.sample.component.WebProxy;
import com.jkuo.sample.config.WebClientConfig;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringJUnitConfig
@ContextConfiguration(classes = { WebProxy.class, WebClientConfig.class })
@ActiveProfiles("local")
public class SmokescreenIT {

    public static final DockerImageName SMOKESCREEN_IMAGE = DockerImageName
            .parse("ghcr.io/jacksonkuo/smokescreen:latest");

    @SuppressWarnings("resource")
    static GenericContainer<?> smokescreen = new GenericContainer<>(SMOKESCREEN_IMAGE)
            .withExposedPorts(4750);

    @BeforeAll
    static void beforeAll() {
        try {
            smokescreen.start();
            Integer smokescreenPort = smokescreen.getMappedPort(4750);
            System.setProperty("spring.smokescreen.host", "localhost");
            System.setProperty("spring.smokescreen.port", smokescreenPort.toString());
            System.err.println("Smokescreen container started.");
        } catch (Exception e) {
            System.err.println("Failed to start Smokescreen container: " + e.getMessage());
            throw e;
        }
    }

    @AfterAll
    static void afterAll() {
        if (smokescreen.isRunning()) {
            smokescreen.stop();
            System.err.println("Smokescreen container stopped.");
        }
    }

    @Autowired
    private WebProxy webProxy;

    @Test
    public void testSmokescreenAllow() {
        System.err.println(smokescreen.getHost() + smokescreen.getMappedPort(4750));
        Mono<String> responseMono = webProxy.retrieveUrl("https://example.com");
        String response = responseMono.block();
        assertThat(response.startsWith("<!doctype html>")).isTrue();
    }

    @Test
    public void testSmokescreenDeny() {
        System.err.println(smokescreen.getHost() + smokescreen.getMappedPort(4750));
        Mono<String> responseMono = webProxy.retrieveUrl("https://example.org");
        String response = responseMono.block();
        assertThat(response.startsWith("Failed to fetch data")).isTrue();
    }

}