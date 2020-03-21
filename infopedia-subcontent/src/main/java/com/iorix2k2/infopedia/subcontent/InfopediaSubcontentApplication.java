package com.iorix2k2.infopedia.subcontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class InfopediaSubcontentApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(InfopediaSubcontentApplication.class, args);
	}
}
