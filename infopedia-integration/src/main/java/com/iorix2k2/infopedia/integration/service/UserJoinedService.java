package com.iorix2k2.infopedia.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.iorix2k2.infopedia.integration.model.User;
import com.iorix2k2.infopedia.util.HttpUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


// Created to avoid circular dependencies.
@Service
public class UserJoinedService
{
	@CircuitBreaker(name = USER_JOINED_SERVICE_CB, fallbackMethod = "removeOnError")
	public User remove(Long id)
	{
		var removeArticleList = articleJoinedService.removeByUserId(id);
		var headers = HttpUtil.createDefaultHeaders();
		headers.setBasicAuth("system", "infopedia_system_1234");
		var httpEntity = new HttpEntity<User>(null, headers);
		var deletedUser = restTemplate.exchange(BY_ID_URL,
				HttpMethod.DELETE, httpEntity, User.class, id).getBody();
		deletedUser.setArticleList(removeArticleList);
		return deletedUser;
	}

	@SuppressWarnings("unused")
	private void removeOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}


	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ArticleJoinedService articleJoinedService;


	private static final String USER_JOINED_SERVICE_CB = "userJoinedServiceCB";

	private static final String USER_URL = "http://infopedia-user-service/api/user";
	private static final String BY_ID_URL = USER_URL + "/{id}";
}
