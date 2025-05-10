package com.jkuo.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import com.jkuo.sample.service.CaptchaService;

@RestController
@RequestMapping(value = "/captcha", produces = "text/html")
public class CaptchaController {

    public CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("")
    public String captchaform() {
        return """
                <html>
                <head>
                    <title>hCaptcha Demo</title>
                    <script src="https://js.hcaptcha.com/1/api.js" async defer></script>
                </head>
                <body>
                    <form action="" method="POST">
                    <div class="h-captcha" data-sitekey="a36e9a82-f33c-4234-be50-533a37283cba"></div>
                    <br />
                    <input type="submit" value="Submit" />
                    </form>
                </body>
                </html>
                """;
    }

    @PostMapping("")
    public String submit(@RequestParam("h-captcha-response") String clientResponse) throws Exception {

        String name = captchaService.verifyCaptcha(clientResponse);
        if (name.equals("true")) {
            return "Captcha successful!";
        } else {
            return "Captcha submission failed!";
        }
    }
}
