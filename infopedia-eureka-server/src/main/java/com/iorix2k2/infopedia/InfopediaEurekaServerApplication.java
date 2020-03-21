package com.iorix2k2.infopedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class InfopediaEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfopediaEurekaServerApplication.class, args);
	}

}
