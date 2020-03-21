package com.iorix2k2.infopedia.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan
@EnableEurekaClient
public class InfopediaUserApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(InfopediaUserApplication.class, args);
	}
}
