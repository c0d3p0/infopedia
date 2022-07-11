package com.iorix2k2.infopedia.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableEurekaClient
@SpringBootApplication
public class InfopediaArticleApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(InfopediaArticleApplication.class, args);
	}
}
