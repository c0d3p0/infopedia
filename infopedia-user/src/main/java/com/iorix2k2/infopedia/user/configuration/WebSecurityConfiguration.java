package com.iorix2k2.infopedia.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.iorix2k2.infopedia.user.authentication.UserAuthenticationProvider;
import com.iorix2k2.infopedia.user.error.RestAuthenticationEntryPoint;
import com.iorix2k2.infopedia.user.error.UserAccessDeninedHandler;
import com.iorix2k2.infopedia.user.model.Role;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration
{
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf().disable().httpBasic().and().sessionManagement().
				sessionCreationPolicy(SessionCreationPolicy.STATELESS).
				and().exceptionHandling().
				authenticationEntryPoint(restAuthenticationEntryPoint).and().
				exceptionHandling().accessDeniedHandler(userAccessDeninedHandler).and().
				authenticationProvider(userAuthenticationProvider).
				authorizeRequests().

				
				antMatchers(HttpMethod.GET, CHECK_TOKEN_OR_FULL_PERMISSION).hasAnyAuthority(
						Role.ROLE_USER_FULL_PERMISSION, Role.ROLE_USER_TOKEN_PERMISSION).
				antMatchers(HttpMethod.GET, CHECK_FULL_PERMISSION).
						hasAuthority(Role.ROLE_USER_FULL_PERMISSION).
				antMatchers(HttpMethod.GET, BY_USERNAME_OR_EMAIL_WITH,
						CHECK_ADMIN_PERMISSION).hasAuthority(Role.ROLE_ADMIN).
				antMatchers(HttpMethod.POST, SING_UP_URL).permitAll().
				
						
				antMatchers(HttpMethod.GET, CHECK_TOKEN_URL, PARTIAL_RANDOM_URL, 
						PARTIAL_BY_USERNAME_WITH_URL, PARTIAL_BY_ID_URL).permitAll().				


				antMatchers(HttpMethod.PATCH, GENERATE_TOKEN_URL).
						hasAuthority(Role.ROLE_USER_FULL_PERMISSION).
				antMatchers(HttpMethod.PATCH, CHANGE_PASSWORD_URL).
						hasAuthority(Role.ROLE_USER_FULL_PERMISSION).

				 
				antMatchers(HttpMethod.GET, YOUR_DATA_URL).hasAnyAuthority(
						Role.ROLE_USER_FULL_PERMISSION, Role.ROLE_USER_TOKEN_PERMISSION).
				antMatchers(HttpMethod.PATCH, EXPIRE_TOKEN_URL, YOUR_DATA_URL).
						hasAnyAuthority(Role.ROLE_USER_FULL_PERMISSION,
								Role.ROLE_USER_TOKEN_PERMISSION).
						
						
				antMatchers(HttpMethod.GET, USER_URL, USER_ID_URL).
						hasAuthority(Role.ROLE_ADMIN).
				antMatchers(HttpMethod.POST, USER_URL).
						hasAuthority(Role.ROLE_ADMIN).
				antMatchers(HttpMethod.PATCH, USER_ID_URL).
						hasAuthority(Role.ROLE_ADMIN).
				antMatchers(HttpMethod.DELETE, USER_ID_URL).
						hasAuthority(Role.ROLE_ADMIN).
						
				
				anyRequest().denyAll();
				return http.build();
	}
	

	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Autowired
	private UserAccessDeninedHandler userAccessDeninedHandler;
	
	
	private static final String SING_UP_URL = "/user/sign-up";
	private static final String CHECK_TOKEN_URL = "/user/check-token";
	private static final String PARTIAL_RANDOM_URL = "/user/partial-random/{amount}";
	private static final String PARTIAL_BY_ID_URL = "/user/partial-by-id/{id}";
	private static final String PARTIAL_BY_USERNAME_WITH_URL =
			"/user/partial-by-username-with/{username}";
	private static final String BY_USERNAME_OR_EMAIL_WITH =
			"/user/by-username-or-email-with/{text}";	

	private static final String GENERATE_TOKEN_URL = "/user/generate-token";
	private static final String CHANGE_PASSWORD_URL = "/user/change-password";
	private static final String CHECK_FULL_PERMISSION = "/user/check-full-permission";

	private static final String YOUR_DATA_URL = "/user/your-data";
	private static final String EXPIRE_TOKEN_URL = "/user/expire-token";
	private static final String CHECK_TOKEN_OR_FULL_PERMISSION =
			"/user/check-token-or-full-permission";
	
	private static final String USER_URL = "/user";
	private static final String USER_ID_URL = "/user/{id}";
	private static final String CHECK_ADMIN_PERMISSION = "/user/check-admin-permission";
}
