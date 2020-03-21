package com.iorix2k2.infopedia.article.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable().sessionManagement().
		sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
		authorizeRequests().
		
		
		antMatchers(HttpMethod.POST, articleURL).permitAll().
		
		antMatchers(HttpMethod.GET, articleURL, randomURL, byUserIdURL,
				byTitleURL, byIdURL, byIdAndUserIdURL).permitAll().
		
		antMatchers(HttpMethod.PATCH, byIdURL).permitAll().
		
		antMatchers(HttpMethod.DELETE, byIdURL, byUserIdURL).permitAll().
		
		
		anyRequest().denyAll();
	}
	
	
	private String articleURL = "/article";
	private String randomURL = "/article/random/{amount}";
	private String byUserIdURL = "/article/by-user-id/{userId}";
	private String byTitleURL = "/article/by-title-with/{title}";
	private String byIdURL = "/article/{id}";
	private String byIdAndUserIdURL = "/article/by-id-and-user-id/{id}/{userId}";
}