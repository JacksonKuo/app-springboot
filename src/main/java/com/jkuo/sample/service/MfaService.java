package com.jkuo.sample.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

@Service
public class MfaService {

    private static final Logger logger = LoggerFactory.getLogger(MfaService.class);

    @Value("${verify.service.sid}")
    private String VERIFY_SERVICE_SID;

    @Value("${twilio.account.sid}")
    private String TWILIO_ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String TWILIO_AUTH_TOKEN;

    public MfaService() {
    }

    public Verification sendVerificationCode(String phone) {
        String verifiedPhone = checkPhoneNumber(phone);

        if (verifiedPhone == null) {
            return null;
        }
        try {
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
            Verification verification = Verification.creator(VERIFY_SERVICE_SID, verifiedPhone, "sms").create();

            return verification;
        } catch (Exception e) {
            logger.info("TWILIO_ACCOUNT_SID isNull: " + Objects.isNull(TWILIO_ACCOUNT_SID));
            logger.info("TWILIO_AUTH_TOKEN: isNull" + Objects.isNull(TWILIO_AUTH_TOKEN));
            throw new RuntimeException("Failed to send verification code: " + e.getMessage(), e);
        }
    }

    public VerificationCheck verifyCode(String code, String phone) {
        try {
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
            VerificationCheck verificationCheck = VerificationCheck.creator(VERIFY_SERVICE_SID)
                    .setTo(phone)
                    .setCode(code)
                    .create();
            return verificationCheck;
        } catch (Exception e) {
            return null;
        }
    }

    public String checkPhoneNumber(String inputNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber phoneNumberProto = phoneUtil.parse(inputNumber, "US");
            if (phoneUtil.isValidNumberForRegion(phoneNumberProto, "US")) {
                return phoneUtil.format(phoneNumberProto, PhoneNumberFormat.E164);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
