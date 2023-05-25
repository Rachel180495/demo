package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.demo")
@PropertySource("classpath:application.properties")
public class DemoApplication {

	public DemoApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
