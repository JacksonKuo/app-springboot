package com.jkuo.sample.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.net.URI;

import com.jkuo.sample.service.MfaService;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

@RestController
public class MfaController {

    public MfaService mfaService;

    public MfaController(MfaService mfaService) {
        this.mfaService = mfaService;
    }

    @GetMapping("/mfa")
    public String showMFAForm() {
        return """
                <html>
                <head>
                    <title>Twilio MFA Demo</title>
                    <script src="https://bakacore.com:8443/mfa" async defer></script>
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
            Verification verification = mfaService.sendVerificationCode(phone);
            if (verification == null) {
                return ResponseEntity.badRequest().body("Invalid phone number");
            }
            if (verification.getStatus().equals("pending")) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("/mfa/verify"))
                        .build();
            } else {
                return ResponseEntity.ok(verification.getStatus());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/mfa/verify")
    public String showVerificationForm() {
        return """
                <html>
                <head>
                    <title>Twilio MFA Demo</title>
                    <script src="https://bakacore.com:8443/mfa/verify" async defer></script>
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
            VerificationCheck verificationCheck = mfaService.verifyCode(code, phone);
            return verificationCheck.getStatus();
        } catch (Exception e) {
            return "verification failed";
        }
    }
}