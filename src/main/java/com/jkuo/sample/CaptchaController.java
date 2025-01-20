package com.jkuo.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/captcha", produces = "text/html")
public class CaptchaController {

	@GetMapping("")
	public String index() {
		return "<html><body><h1>Captcha</h1></body></html>";
	}

}