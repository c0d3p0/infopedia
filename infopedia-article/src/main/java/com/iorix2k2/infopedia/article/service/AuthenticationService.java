package com.iorix2k2.infopedia.article.service;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.iorix2k2.infopedia.util.HttpUtil;



@Service
public class AuthenticationService
{
	@CircuitBreaker(name = "authenticationServiceCB", fallbackMethod = "checkPermissionOnError")
	public void checkAdminPermission(HttpServletRequest request)
	{
		var headers = HttpUtil.getHeadersFromRequest(request);
		dispatchService.dispatch(CHECK_ADMIN_PERMISSION_URL,
				HttpMethod.GET, headers, null, String.class, null);
	}

	@SuppressWarnings("unused")
	private void checkPermissionOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}


	@Autowired
	private DispatchService dispatchService;
	
	private static final String CHECK_ADMIN_PERMISSION_URL =
			"http://infopedia-user-service/api/user/check-admin-permission";
}
