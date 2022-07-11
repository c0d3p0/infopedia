package com.iorix2k2.infopedia.integration.service;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.iorix2k2.infopedia.integration.model.Article;
import com.iorix2k2.infopedia.integration.model.Gender;
import com.iorix2k2.infopedia.integration.model.User;


@Service
public class UserService
{
	@CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "getSingleOnError")
	public User getPartialById(Long id)
	{
		return restTemplate.getForObject(PARTIAL_BY_ID_URL, User.class, id);
	}

	@CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "checkPermissionOnError")
	public User checkAdminPermission(HttpHeaders headers)
	{
		return dispatchService.dispatch(CHECK_ADMIN_PERMISSION_URL,
				HttpMethod.GET, headers, null, User.class, null).getBody();
	}

	@CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "checkPermissionOnError")
	public User checkFullPermission(HttpHeaders headers)
	{
		return dispatchService.dispatch(CHECK_FULL_PERMISSION_URL,
				HttpMethod.GET, headers, null, User.class, null).getBody();
	}

	@CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "checkPermissionOnError")
	public User checkTokenOrFullPermission(HttpHeaders headers)
	{
		return dispatchService.dispatch(CHECK_TOKEN_OR_FULL_PERMISSION_URL,
				HttpMethod.GET, headers, null, User.class, null).getBody();
	}

	@SuppressWarnings("unused")
	private User getSingleOnError(Throwable t)
	{
		return getUnavailable(-1L);
	}

	@SuppressWarnings("unused")
	private void checkPermissionOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}

	public User getUnavailable(Long id)
	{
		var user = new User();
		user.setId(id);
		user.setUsername("Unavailable");
		user.setAge(-1);
		user.setArticleList(new ArrayList<Article>());
		user.setCountry("Unavailable");
		user.setEmail("unavailable@unavailable.com");
		user.setFullName("Unavailable");
		user.setGender(Gender.MALE);
		user.setPassword("Unavailable");
		user.setToken("Unavailable");
		user.setTokenActiveTime(0L);
		user.setSystemAdmin(false);
		return user;
	}


	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DispatchService dispatchService;


	private static final String USER_SERVICE_CB = "userServiceCB";
	private static final String USER_URL = "http://infopedia-user-service/api/user";
	private static final String PARTIAL_BY_ID_URL = USER_URL + "/partial-by-id/{id}";
	private static final String CHECK_ADMIN_PERMISSION_URL =
			USER_URL + "/check-admin-permission";
	private static final String CHECK_FULL_PERMISSION_URL =
			USER_URL + "/check-full-permission";
	private static final String CHECK_TOKEN_OR_FULL_PERMISSION_URL =
			USER_URL + "/check-token-or-full-permission";
}