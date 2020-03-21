package com.iorix2k2.infopedia.article.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.iorix2k2.infopedia.article.error.RestResponseErrorHandler;


@Configuration
public class InfopediaArticleBeanRegister
{	
	@Bean
	public Validator validator()
	{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
	
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
		rt.setErrorHandler(restResponseErrorHander);
		rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return rt;
	}
	
	
	@Autowired
	private RestResponseErrorHandler restResponseErrorHander;
}
