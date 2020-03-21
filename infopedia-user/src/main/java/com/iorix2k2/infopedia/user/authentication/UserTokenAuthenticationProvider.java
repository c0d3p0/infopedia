package com.iorix2k2.infopedia.user.authentication;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.iorix2k2.infopedia.user.model.Role;
import com.iorix2k2.infopedia.user.model.User;
import com.iorix2k2.infopedia.user.service.UserService;


@Component
public class UserTokenAuthenticationProvider implements AuthenticationProvider
{
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException
	{
	  String user = authentication.getName().toLowerCase();
	  String token = authentication.getCredentials().toString();
	  Optional<User> o = userService.checkToken(user, token);
	  
	  if(!o.isEmpty())
	  {
	  	User u = o.get();
	  	return new UsernamePasswordAuthenticationToken(u, u.getToken(),
	  			Arrays.asList(new SimpleGrantedAuthority(Role.ROLE_USER_TOKEN_PERMISSION)));
	  }
	  
	  return null;	  	
	}
	
	@Override
	public boolean supports(Class<?> authentication)
	{
		return true;
	}
	
	
	@Autowired
	private UserService userService;
}