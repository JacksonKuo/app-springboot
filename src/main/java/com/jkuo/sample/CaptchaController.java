package com.jkuo.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

	@GetMapping("")
	public String index() {
		return "Captcha";
	}

}