package com.iorix2k2.infopedia.subcontent.configuration;

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
		
		
		antMatchers(HttpMethod.POST, subContentURL).permitAll().
		
		antMatchers(HttpMethod.GET, subContentURL, byArticleIdURL, byIdURL)
				.permitAll().
				
		antMatchers(HttpMethod.PATCH, byIdURL)
				.permitAll().
				
		antMatchers(HttpMethod.DELETE, byIdURL, byArticleIdURL,
				byUserIdURL).permitAll().
		
		
		anyRequest().denyAll();
	}
	
	
	private String subContentURL = "/sub-content";
	private String byArticleIdURL = "/sub-content/by-article-id/{articleId}";
	private String byIdURL = "/sub-content/{id}";
	private String byUserIdURL = "/sub-content/by-user-id/{userId}";
}