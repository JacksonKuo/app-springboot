package com.jkuo.sample.integration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.boot.test.context.SpringBootTest;

import com.jkuo.sample.service.MfaService;

import org.springframework.beans.factory.annotation.Autowired;

@Tag("integration")
@SpringBootTest
@SpringJUnitConfig
@ContextConfiguration(classes = { MfaService.class })
@ActiveProfiles("local")
public class MfaServiceIT {

    @Autowired
    MfaService mfaService;

    @Test
    public void test() {
        System.out.println("MfaServiceIT test");
    }
}
