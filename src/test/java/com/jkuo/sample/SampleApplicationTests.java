package com.jkuo.sample;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class SampleApplicationTests {

	// test only fails if the context fails to load
	@Test
	void contextLoads() {
	}

}
