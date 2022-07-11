package com.iorix2k2.infopedia.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

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
	public DateFormat defaultDateFormat()
	{
		var df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df;
	}

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
