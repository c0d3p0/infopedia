package com.iorix2k2.infopedia.integration.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.iorix2k2.infopedia.integration.model.Catalogue;
import com.iorix2k2.infopedia.integration.model.SubContent;
import com.iorix2k2.infopedia.integration.model.SubContentType;
import com.iorix2k2.infopedia.util.HttpUtil;


@Service
public class SubContentService
{
	@CircuitBreaker(name = SUBCONTENT_SERVICE_CB, fallbackMethod = "getSingleOnError")
	public SubContent getById(Long id)
	{
		return restTemplate.exchange(BY_ID_URL, HttpMethod.GET, null,
				SubContent.class, id).getBody();
	}

	@CircuitBreaker(name = SUBCONTENT_SERVICE_CB, fallbackMethod = "getMultipleOnError")
	public List<SubContent> getByArticleId(Long articleId)
	{
		var ptr = new ParameterizedTypeReference<Catalogue<SubContent>>(){};
		return restTemplate.exchange(BY_ARTICLE_ID_URL,
				HttpMethod.GET, null, ptr, articleId).getBody().getList();
	}

	@CircuitBreaker(name = SUBCONTENT_SERVICE_CB, fallbackMethod = "getMultipleOnError")
	public List<SubContent> getByContentWith(String content)
	{
		var ptr = new ParameterizedTypeReference<Catalogue<SubContent>>(){};
		return restTemplate.exchange(BY_CONTENT_WITH_URL,
				HttpMethod.GET, null, ptr, content).getBody().getList();
	}

	@CircuitBreaker(name = SUBCONTENT_SERVICE_CB, fallbackMethod = "removeOnError")
	public List<SubContent> removeByUserId(Long userId)
	{
		var hh = HttpUtil.createDefaultHeaders();
		hh.setBasicAuth("system", "infopedia_system_1234");
		var he = new HttpEntity<SubContent>(null, hh);
		var ptr = new ParameterizedTypeReference<Catalogue<SubContent>>(){};
		return restTemplate.exchange(BY_USER_ID_URL,
				HttpMethod.DELETE, he, ptr, userId).getBody().getList();
	}

	@SuppressWarnings("unused")
	private SubContent getSingleOnError(Throwable t)
	{
		return getUnavailable(-1L, -1L, -1L);
	}

	@SuppressWarnings("unused")
	private List<SubContent> getMultipleOnError(Throwable t)
	{
		return Arrays.asList(getUnavailable(-1L, -1L, -1L));
	}

	@SuppressWarnings("unused")
	private SubContent removeOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}

	public SubContent getUnavailable(Long id, Long userId, Long articleId)
	{
		var sc = new SubContent();
		sc.setId(id);
		sc.setArticleId(articleId);
		sc.setContent("Content unavailable");
		sc.setPosition((short)1);
		sc.setTitle("Unavailable");
		sc.setType(SubContentType.TEXT);
		sc.setUserId(userId);
		return sc;
	}


	@Autowired
	private RestTemplate restTemplate;


	private static final String SUBCONTENT_SERVICE_CB = "subContentServiceCB";

	private static final String SUBCONTENT_URL =
			"http://infopedia-sub-content-service/api/sub-content";
	private static final String BY_ID_URL = SUBCONTENT_URL + "/{id}";
	private static final String BY_ARTICLE_ID_URL =
			SUBCONTENT_URL + "/by-article-id/{articleId}";
	private static final String BY_USER_ID_URL =
			SUBCONTENT_URL + "/by-user-id/{userId}";
	private static final String BY_CONTENT_WITH_URL =
			SUBCONTENT_URL + "/by-content-with/{content}";
}
