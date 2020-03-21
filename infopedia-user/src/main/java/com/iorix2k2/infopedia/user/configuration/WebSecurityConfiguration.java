package com.iorix2k2.infopedia.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.iorix2k2.infopedia.user.authentication.UserPasswordAuthenticationProvider;
import com.iorix2k2.infopedia.user.authentication.UserTokenAuthenticationProvider;
import com.iorix2k2.infopedia.user.exception.RestAuthenticationEntryPoint;
import com.iorix2k2.infopedia.user.model.Role;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{      
		http.csrf().disable().httpBasic().and().sessionManagement().
				sessionCreationPolicy(SessionCreationPolicy.STATELESS).
				and().exceptionHandling().
				authenticationEntryPoint(restAuthenticationEntryPoint).and().
				authenticationProvider(userPasswordAuthenticationProvider).
				authenticationProvider(userTokenAuthenticationProvider).
				authorizeRequests().

				
				antMatchers(HttpMethod.GET, checkTokenOrFullPermission).hasAnyAuthority(
						Role.ROLE_USER_FULL_PERMISSION, Role.ROLE_USER_TOKEN_PERMISSION).
				antMatchers(HttpMethod.GET, checkFullPermission).
						hasAuthority(Role.ROLE_USER_FULL_PERMISSION).
				antMatchers(HttpMethod.GET, checkAdminPermission).
						hasAuthority(Role.ROLE_ADMIN).
				antMatchers(HttpMethod.POST, signUpURL).permitAll().
				
						
				antMatchers(HttpMethod.GET, checkTokenURL, dataReducedRandomURL, 
						dataReducedByUsernameWithURL, dataReducedByIdURL).permitAll().				


				antMatchers(HttpMethod.PATCH, generateTokenURL).
						hasAuthority(Role.ROLE_USER_FULL_PERMISSION).
				antMatchers(HttpMethod.PATCH, changePasswordURL).
						hasAuthority(Role.ROLE_USER_FULL_PERMISSION).

				 
				antMatchers(HttpMethod.GET, yourDataURL).hasAnyAuthority(
						Role.ROLE_USER_FULL_PERMISSION, Role.ROLE_USER_TOKEN_PERMISSION).
				antMatchers(HttpMethod.PATCH, expireTokenURL, yourDataURL).
						hasAnyAuthority(Role.ROLE_USER_FULL_PERMISSION,
								Role.ROLE_USER_TOKEN_PERMISSION).
						
						
				antMatchers(HttpMethod.GET, userURL, userIdURL).
						hasAuthority(Role.ROLE_ADMIN).
				antMatchers(HttpMethod.POST, userURL).
						hasAuthority(Role.ROLE_ADMIN).
				antMatchers(HttpMethod.PATCH, userIdURL).
						hasAuthority(Role.ROLE_ADMIN).
				antMatchers(HttpMethod.DELETE, userIdURL).
						hasAuthority(Role.ROLE_ADMIN).
						
						
				anyRequest().denyAll();		
	}
	
  
	@Autowired
	private UserPasswordAuthenticationProvider userPasswordAuthenticationProvider;
	
	@Autowired
	private UserTokenAuthenticationProvider userTokenAuthenticationProvider;
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	
	private String signUpURL = "/user/sign-up";
	private String checkTokenURL = "/user/check-token";
	private String dataReducedRandomURL = "/user/data-reduced-random/{amount}";
	private String dataReducedByUsernameWithURL = 
			"/user/data-reduced-by-username-with/{username}";
	private String dataReducedByIdURL = "/user/data-reduced-by-id/{id}";	

	private String generateTokenURL = "/user/generate-token";
	private String changePasswordURL = "/user/change-password/{id}";
	private String checkFullPermission = "/user/check-full-permission";

	private String yourDataURL = "/user/your-data/{id}";
	private String expireTokenURL = "/user/expire-token";
	private String checkTokenOrFullPermission =
			"/user/check-token-or-full-permission";
	
	private String userURL = "/user";
	private String userIdURL = "/user/{id}";
	private String checkAdminPermission = "/user/check-admin-permission";
}
