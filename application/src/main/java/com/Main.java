package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@ServletComponentScan // @WebListener 스캔을 가능하도록
@SpringBootApplication
public class Main extends SpringBootServletInitializer {

	// for war 생성
	 @Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	 	return builder.sources(Main.class);
	 }

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
