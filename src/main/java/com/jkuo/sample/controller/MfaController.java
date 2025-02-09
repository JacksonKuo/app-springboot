package com.jkuo.sample.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;



@RestController
public class MfaController {

    @Value("${verify.service.sid}")
    private String VERIFY_SERVICE_SID;

    @Value("${twilio.account.sid}")
    private String TWILIO_ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String TWILIO_AUTH_TOKEN;

    @GetMapping("/mfa")
    public String showMFAForm() {
		return """
        <html>
        <head>
            <title>Twilio MFA Demo</title>
            <script src="https://bakacore.com:8087/mfa" async defer></script>
        </head>
        <body>
            <form action="/mfa" method="POST">
                <label for="phone">Phone Number:</label>
                <input type="tel" id="phone" name="phone" required />
                <br />
                <input type="submit" value="Submit" />
            </form>
        </body>
        </html>
        """;
	}

    @PostMapping("/mfa")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String phone) {
        try {
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
            Verification verification = Verification.creator(VERIFY_SERVICE_SID, phone, "sms").create();
            if (verification.getStatus().equals("pending")) {
                return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/mfa/verify"))
                .build();
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(verification.getStatus());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("mfa failed");
        }
	}

    @GetMapping("/mfa/verify")
    public String showVerificationForm() {
		return """
        <html>
        <head>
            <title>Twilio MFA Demo</title>
            <script src="https://bakacore.com:8087/mfa/verify" async defer></script>
        </head>
        <body>
            <form action="/mfa/verify" method="POST">
                <label for="code">Code:</label>
                <input type="text" id="code" name="code" required />
                <label for="phone">Phone:</label>
                <input type="tel" id="phone" name="phone" required />
                <br />
                <input type="submit" value="Submit" />
            </form>
        </body>
        </html>
        """;
	}

    @PostMapping("/mfa/verify")
    public String verifyCode(@RequestParam String code, @RequestParam String phone) {
        try {
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
            VerificationCheck verificationCheck = VerificationCheck.creator(VERIFY_SERVICE_SID)
                .setTo(phone)
                .setCode(code)
                .create();

            return verificationCheck.getStatus();
        } catch (Exception e) {
            return "verification failed";
        }
	}

}