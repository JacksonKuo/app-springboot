package com.jkuo.sample.unit.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.MediaType;

import com.jkuo.sample.service.CaptchaService;
import com.jkuo.sample.controller.CaptchaController;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

@Tag("unit")
@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@WebMvcTest(CaptchaController.class)
public class CaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CaptchaService captchaService;

    @Test
    public void testSubmitSuccess() throws Exception {
        when(captchaService.verifyCaptcha("test")).thenReturn("true");

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("h-captcha-response=test"))
                .andExpect(content().string("Captcha successful!"));
    }

    @Test
    public void testSubmiFailure() throws Exception {
        when(captchaService.verifyCaptcha("test")).thenReturn("false");

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("h-captcha-response=test"))
                .andExpect(content().string("Captcha submission failed!"));
    }
}
