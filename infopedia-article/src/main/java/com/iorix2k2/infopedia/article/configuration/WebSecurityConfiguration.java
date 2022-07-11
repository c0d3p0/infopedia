package com.iorix2k2.infopedia.article.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.iorix2k2.infopedia.article.error.RestAuthenticationEntryPoint;


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
				
				
				antMatchers(HttpMethod.POST, ARTICLE_URL).permitAll().
				
				antMatchers(HttpMethod.GET, ARTICLE_URL, RANDOM_URL,
						BY_USER_ID_URL, BY_TITLE_URL, BY_CONTENT_URL,
						BY_ID_URL, BY_IDS_URL, BY_ID_AND_USER_ID_URL).permitAll().
				
				antMatchers(HttpMethod.PATCH, BY_ID_URL).permitAll().
				
				antMatchers(HttpMethod.DELETE, BY_ID_URL, BY_USER_ID_URL).permitAll().
				
				anyRequest().denyAll();

		return http.build();
	}


	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


	private static final String ARTICLE_URL = "/article";
	private static final String RANDOM_URL = "/article/random/{amount}";
	private static final String BY_USER_ID_URL = "/article/by-user-id/{userId}";
	private static final String BY_TITLE_URL = "/article/by-title-with/{title}";
	private static final String BY_CONTENT_URL = "/article/by-content-with/{content}";
	private static final String BY_ID_URL = "/article/{id}";
	private static final String BY_IDS_URL = "/article/by-ids/{ids}";
	private static final String BY_ID_AND_USER_ID_URL =
			"/article/by-id-and-user-id/{id}/{userId}";

			
}