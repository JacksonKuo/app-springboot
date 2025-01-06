package com.jkuo.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SampleApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SampleApplication.class, args);

		//Cat cat = context.getBean("BlueCat", Cat.class);
		//System.out.println(cat.getName());

		Person person = context.getBean("Alice", Person.class);
		System.out.println(person.getCat().getName());
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}

}
