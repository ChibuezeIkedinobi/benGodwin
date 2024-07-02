package com.drinks.BenGodwin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = "com.drinks.BenGodwin")
@EnableWebSecurity
public class BenGodwinApplication {

	public static void main(String[] args) {
		SpringApplication.run(BenGodwinApplication.class, args);
	}

}
