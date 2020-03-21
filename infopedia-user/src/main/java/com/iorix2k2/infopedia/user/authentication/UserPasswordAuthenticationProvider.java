package com.iorix2k2.infopedia.user.authentication;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.iorix2k2.infopedia.user.model.Role;
import com.iorix2k2.infopedia.user.model.User;
import com.iorix2k2.infopedia.user.service.UserService;


@Component
public class UserPasswordAuthenticationProvider implements AuthenticationProvider
{
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException
	{
	  String user = authentication.getName().toLowerCase();
	  String password = authentication.getCredentials().toString();

	  if(user.equalsIgnoreCase(adminUser))
	  { 
	  	if(passwordEncoder.matches(password, adminPass))
	  	{
	  		User admin = new User();
	  		admin.setId(-1L);
	  		admin.setUsername(adminUser);
	  		admin.setPassword(adminPass);
	  		return new UsernamePasswordAuthenticationToken(admin, password,
	  				Arrays.asList(new SimpleGrantedAuthority(Role.ROLE_ADMIN)));
	  	}
	  	
	  	return null;
	  }
	  else
	  {
	  	Optional<User> o = userService.findByUserAndPassword(user, password);
	  	
	  	if(!o.isEmpty())
	  	{
	  		User u = o.get();
	  	
	  		return new UsernamePasswordAuthenticationToken(u, u.getPassword(),
	  				Arrays.asList(new SimpleGrantedAuthority(Role.ROLE_USER_FULL_PERMISSION)));	  				
	  	}
	  	
	  	return null;
	  }
	}
	
	@Override
	public boolean supports(Class<?> authentication)
	{
		return (UsernamePasswordAuthenticationToken.class.
        isAssignableFrom(authentication));
	}
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	private String adminUser = "system-admin";
	private String adminPass = "$2y$12$e4hXWTOIOpBCTme8lCgx3OsQNutNmhl036cdcc9NBLhspcb.fsvsK";
}