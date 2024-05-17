package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.security.Principal;

@Configuration
@EnableAsync
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

//	@Bean
//	public ServerEndpointExporter serverEndpointExporter() {
//		return new ServerEndpointExporter();
//	}
//
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		servletContext.setInitParameter("org.apache.tomcat.websocket.textBuffer.Size", "1024000");
//	}
}
