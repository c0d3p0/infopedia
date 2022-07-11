package com.iorix2k2.infopedia.subcontent.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.iorix2k2.infopedia.subcontent.error.RestAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration
{
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf().disable().sessionManagement().
				sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
				exceptionHandling().
				authenticationEntryPoint(restAuthenticationEntryPoint).and().
				authorizeRequests().
		
		
				antMatchers(HttpMethod.POST, SUB_CONTENT_URL).permitAll().
				
				antMatchers(HttpMethod.GET, SUB_CONTENT_URL,
						BY_ARTICLE_ID_URL, BY_CONTENT_URL, BY_ID_URL).permitAll().
						
				antMatchers(HttpMethod.PATCH, BY_ID_URL)
						.permitAll().
						
				antMatchers(HttpMethod.DELETE, BY_ID_URL,
						BY_ARTICLE_ID_URL, BY_USER_ID_URL).permitAll().
				
				
				anyRequest().denyAll();

		return http.build();
	}

	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


	private static final String SUB_CONTENT_URL = "/sub-content";
	private static final String BY_ARTICLE_ID_URL =
			"/sub-content/by-article-id/{articleId}";
	private static final String BY_CONTENT_URL =
			"/sub-content/by-content-with/{content}";
	private static final String BY_ID_URL = "/sub-content/{id}";
	private static final String BY_USER_ID_URL = "/sub-content/by-user-id/{userId}";
}