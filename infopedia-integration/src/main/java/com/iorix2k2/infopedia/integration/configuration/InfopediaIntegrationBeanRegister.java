package com.iorix2k2.infopedia.integration.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.iorix2k2.infopedia.integration.error.RestResponseErrorHandler;


@Configuration
public class InfopediaIntegrationBeanRegister
{
	@Bean
	public DateFormat defaultDateFormat()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df;
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate()
	{
		RestTemplate rt = new RestTemplate();
		rt.setErrorHandler(restResponseErrorHandler);
		rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return rt;
	}
	
	
	@Autowired
	private RestResponseErrorHandler restResponseErrorHandler;
}
