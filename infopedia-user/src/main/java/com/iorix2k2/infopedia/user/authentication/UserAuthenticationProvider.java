package com.iorix2k2.infopedia.user.authentication;

import java.util.ArrayList;
import java.util.List;

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
public class UserAuthenticationProvider implements AuthenticationProvider
{
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException
	{
		var user = authentication.getName().toLowerCase();
		var password = authentication.getCredentials().toString();
		var auth = authenticateByUserAndPassword(user, password);
		return auth != null ? auth : authenticateByUserAndToken(user, password);
	}
	
	@Override
	public boolean supports(Class<?> authentication)
	{
		return (UsernamePasswordAuthenticationToken.class.
				isAssignableFrom(authentication));
	}

	private Authentication authenticateByUserAndPassword(String user, String password)
	{
		var o = userService.getByUserAndPassword(user, password);

		if(!o.isEmpty())
		{
			var u = o.get();
			var roles = createRoles(u, true);
			var sgaList = createSimpleGrantedAuthorityList(roles);
			return new UsernamePasswordAuthenticationToken(u, u.getPassword(), sgaList);
		}

		return null;
	}
	
	private Authentication authenticateByUserAndToken(String user, String token)
	{
		var o = userService.checkToken(user, token);
		
		if(!o.isEmpty())
		{
			var u = o.get();
			var roles = createRoles(u, false);
			var sgaList = createSimpleGrantedAuthorityList(roles);
			return new UsernamePasswordAuthenticationToken(u, u.getPassword(), sgaList);
		}

		return null;
	}

	private String[] createRoles(User user, boolean fullPermission)
	{
		var normalRole = fullPermission ?
				Role.ROLE_USER_FULL_PERMISSION : Role.ROLE_USER_TOKEN_PERMISSION;
		return user.isSystemAdmin() ?
				new String[]{Role.ROLE_ADMIN, normalRole} : new String[]{normalRole};
	}

	private List<SimpleGrantedAuthority> createSimpleGrantedAuthorityList(String[] roles)
	{
		var sgaList = new ArrayList<SimpleGrantedAuthority>();
		
		for(var role : roles)
			sgaList.add(new SimpleGrantedAuthority(role));

		return sgaList;
	}


	@Autowired
	private UserService userService;
}
