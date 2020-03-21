package com.iorix2k2.infopedia.integration.configuration;

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
		
		antMatchers(HttpMethod.GET, userCheckAdminPermissionURL).permitAll().
		
		antMatchers(HttpMethod.POST, articleFromUserCURL,
				subContentFromUserCURL).permitAll().
		
		antMatchers(HttpMethod.GET, articleByIdURL).permitAll().
		
		antMatchers(HttpMethod.PATCH, articleFromUserUDURL,
				subContentFromUserUDURL).permitAll().
		
		antMatchers(HttpMethod.DELETE, userByIdURL, userFromUserURL, articleByIdURL, 
				articleFromUserUDURL, subContentFromUserUDURL).permitAll().
		
		anyRequest().denyAll();
	}
	
	
	private String userCheckAdminPermissionURL = "/user/check-admin-permission";
	private String userFromUserURL = "/user/from-user/{id}";
	private String userByIdURL = "/user/{id}";
	
	private String articleByIdURL = "/article/{id}";
	private String articleFromUserCURL = "/article/from-user";
	private String articleFromUserUDURL = "/article/from-user/{id}";
	
	private String subContentFromUserCURL = "/sub-content/from-user";
	private String subContentFromUserUDURL = "/sub-content/from-user/{id}";
}