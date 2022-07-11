package com.iorix2k2.infopedia.user.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.iorix2k2.infopedia.user.error.GlobalExceptionResolver;


@Configuration
public class InfopediaUserBeanRegister
{
	@Bean
	public Validator validator()
	{
		var factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DateFormat defaultDateFormat()
	{
		var df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df;
	}

	@Bean
	public HandlerExceptionResolver errorHandler()
	{
		return new GlobalExceptionResolver();
	}
}
