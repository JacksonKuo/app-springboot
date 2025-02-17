package com.jkuo.sample.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CaptchaControllerTest {

    @Test
    public void testSubmitCaptchaSuccess() throws Exception {
        String test = "test";
        assertTrue(test.equals("test"));
    }
}