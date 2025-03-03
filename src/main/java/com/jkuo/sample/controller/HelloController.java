package com.jkuo.sample.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

	@GetMapping("/")
	public String index() {
		logger.info("Received request for '/' endpoint");
		return "Greetings user!";
	}

}