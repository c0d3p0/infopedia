package com.iorix2k2.infopedia.integration.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.iorix2k2.infopedia.integration.error.RestAuthenticationEntryPoint;


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
				
				antMatchers(HttpMethod.GET, USER_CHECK_ADMIN_PERMISSION_URL).permitAll().
				
				antMatchers(HttpMethod.POST, ARTICLE_BY_USER_URL,
						SUBCONTENT_BY_USER_URL).permitAll().
				
				antMatchers(HttpMethod.GET, ARTICLE_BY_ID_URL,
						ARTICLE_BY_SUBCONTENT_WITH_URL).permitAll().
				
				antMatchers(HttpMethod.PATCH, ARTICLE_FROM_USER_PATCH_URL,
						SUBCONTENT_BY_USER_PATCH_URL).permitAll().
				
				antMatchers(HttpMethod.DELETE, USER_BY_ID_URL,
						USER_REMOVE_ACCOUNT_URL, ARTICLE_BY_ID_URL, 
						ARTICLE_FROM_USER_PATCH_URL, SUBCONTENT_BY_USER_PATCH_URL).permitAll().
				
				anyRequest().denyAll();

		return http.build();
	}

	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


	private static final String USER_CHECK_ADMIN_PERMISSION_URL = "/user/check-admin-permission";
	private static final String USER_REMOVE_ACCOUNT_URL = "/user/remove-account";
	private static final String USER_BY_ID_URL = "/user/{id}";
	
	private static final String ARTICLE_BY_ID_URL = "/article/{id}";
	private static final String ARTICLE_BY_SUBCONTENT_WITH_URL =
			"/article/by-sub-content-with/{content}";

	private static final String ARTICLE_BY_USER_URL = "/article/by-user";
	private static final String ARTICLE_FROM_USER_PATCH_URL = "/article/by-user/{id}";
	
	private static final String SUBCONTENT_BY_USER_URL = "/sub-content/by-user";
	private static final String SUBCONTENT_BY_USER_PATCH_URL = "/sub-content/by-user/{id}";
}