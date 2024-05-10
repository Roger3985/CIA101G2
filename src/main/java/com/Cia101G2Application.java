package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories("com.*")
@EntityScan(basePackages = {"com.*"})
@ServletComponentScan
@EnableScheduling
@SpringBootApplication
public class Cia101G2Application {

	public static void main(String[] args) {
		SpringApplication.run(Cia101G2Application.class, args);
	}

}
