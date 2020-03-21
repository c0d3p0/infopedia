package com.iorix2k2.infopedia.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.iorix2k2.infopedia.error.RestResponseErrorHandler;


@Configuration
public class InfopediaFrontendBeanRegister
{
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate()
	{
		RestTemplate rt = new RestTemplate();
		rt.setErrorHandler(new RestResponseErrorHandler());
		rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return rt;
	}
}
