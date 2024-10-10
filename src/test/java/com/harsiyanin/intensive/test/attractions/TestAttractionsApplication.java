package com.harsiyanin.intensive.test.attractions;

import org.springframework.boot.SpringApplication;

public class TestAttractionsApplication {

	public static void main(String[] args) {
		SpringApplication.from(AttractionsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
