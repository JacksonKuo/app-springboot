package com.jkuo.sample.integration.service;

import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.boot.test.context.SpringBootTest;

import com.jkuo.sample.service.MfaService;
import com.twilio.rest.verify.v2.service.Verification;

import org.springframework.beans.factory.annotation.Autowired;

@Tag("integration")
@SpringBootTest
@SpringJUnitConfig
@ContextConfiguration(classes = { MfaService.class })
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "verify.service.sid=${VERIFY_SERVICE_SID}",
        "twilio.account.sid=${TWILIO_TEST_ACCOUNT_SID}",
        "twilio.auth.token=${TWILIO_TEST_AUTH_TOKEN}"
})
public class MfaServiceIT {

    @Autowired
    MfaService mfaService;

    @Test
    @Disabled
    public void testVerification() {
        // arrange
        String phone = "+15005550006";
        // act
        Verification verification = mfaService.sendVerificationCode(phone);
        // assert
        assert (verification.getStatus().equals("pending"));
    }

}
