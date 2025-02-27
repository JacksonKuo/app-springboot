package com.jkuo.sample.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import java.net.URI;

import org.mockito.Mock;
import org.mockito.InjectMocks;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jkuo.sample.service.MfaService;
import com.jkuo.sample.controller.MfaController;
import com.twilio.rest.verify.v2.service.Verification;

@Tag("unit")
@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
public class MfaControllerTest {

    @Mock
    MfaService mfaService;

    @InjectMocks
    MfaController mfacontroller;

    @Test
    public void testMfaServiceSuccessfulPending() {
        // arrange
        Verification mockVerified = mock(Verification.class);
        given(mfaService.sendVerificationCode(any())).willReturn(mockVerified);
        given(mockVerified.getStatus()).willReturn("pending");
        // act
        String phone = "202-555-0100";
        ResponseEntity<String> resp = mfacontroller.sendVerificationCode(phone);
        // assert
        assertEquals(HttpStatus.FOUND, resp.getStatusCode());
        assertEquals(URI.create("/mfa/verify"), resp.getHeaders().getLocation());
        verify(mfaService).sendVerificationCode(phone);
    }

    @Test
    public void testMfaServicNullVerification() {
        // arrange
        given(mfaService.sendVerificationCode("1111")).willReturn(null);
        // act
        String phone = "1111";
        ResponseEntity<String> resp = mfacontroller.sendVerificationCode(phone);
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        verify(mfaService).sendVerificationCode(phone);
    }
}
