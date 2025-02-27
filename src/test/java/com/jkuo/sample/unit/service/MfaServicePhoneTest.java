package com.jkuo.sample.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.jkuo.sample.service.MfaService;

@Tag("unit")
@SpringJUnitConfig
@ContextConfiguration(classes = { MfaService.class })
@ActiveProfiles("local")
public class MfaServicePhoneTest {

    @Autowired
    MfaService mfaService;

    @Test
    public void testGoodUSPhoneNumber() {
        // arrange
        String phoneNumber = "202-555-0100";
        // act
        String result = mfaService.checkPhoneNumber(phoneNumber);
        // assert
        assert (result.equals("+12025550100"));
    }

    @Test
    public void testBadUSPhoneNumber() {
        // arrange
        String phoneNumber = "+1 416-555-0199";
        // act
        String result = mfaService.checkPhoneNumber(phoneNumber);
        // assert
        assertNull(result);
    }

    @Test
    public void testBadPhoneNumber() {
        // arrange
        String phoneNumber = "1111";
        // act
        String result = mfaService.checkPhoneNumber(phoneNumber);
        // assert
        assertNull(result);
    }

}
