package com.iorix2k2.infopedia.subcontent.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.iorix2k2.infopedia.subcontent.error.GlobalExceptionResolver;
import com.iorix2k2.infopedia.subcontent.error.RestResponseErrorHandler;


@Configuration
public class InfopediaSubContentBeanRegister
{
	@Bean
	public Validator validator()
	{
		var factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
	
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
		var rt = new RestTemplate();
		rt.setErrorHandler(restResponseErrorHandler);
		rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return rt;
	}

	@Bean
	public HandlerExceptionResolver exceptionResolver()
	{
		return new GlobalExceptionResolver();
	}


	@Autowired
	private RestResponseErrorHandler restResponseErrorHandler;
}
