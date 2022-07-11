package com.iorix2k2.infopedia.integration.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.iorix2k2.infopedia.integration.model.Article;
import com.iorix2k2.infopedia.integration.model.Catalogue;
import com.iorix2k2.infopedia.integration.model.SubContent;
import com.iorix2k2.infopedia.util.HttpUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
public class SubContentJoinedService
{
	@CircuitBreaker(name = SUBCONTENT_JOINED_SERVICE_CB, fallbackMethod = "addOnError")
	public SubContent addByUser(Long userId, SubContent subContent)
	{
		subContent.setUserId(userId);
		var articleId = subContent.getArticleId();
		var he = createPayloadWithUserPermissionCheck(subContent, userId, articleId);
		return restTemplate.exchange(SUBCONTENT_URL,
				HttpMethod.POST, he, SubContent.class).getBody();
	}

	@CircuitBreaker(name = SUBCONTENT_JOINED_SERVICE_CB, fallbackMethod = "updateOnError")
	public SubContent updateByUser(Long userId, SubContent subContent)
	{
		subContent.setUserId(null);
		subContent.setArticleId(null);
		var subContentFound = subContentService.getById(subContent.getId());
		var articleId = subContentFound.getArticleId();
		var he = createPayloadWithUserPermissionCheck(subContent, userId, articleId);
		return restTemplate.exchange(BY_ID_URL, HttpMethod.PATCH,
				he, SubContent.class, subContent.getId()).getBody();
	}

	@CircuitBreaker(name = SUBCONTENT_JOINED_SERVICE_CB, fallbackMethod = "removeOnError")
	public SubContent removeByUser(Long id, Long userId)
	{
		var subContentFound = subContentService.getById(id);
		var articleId = subContentFound.getArticleId();
		var he = createPayloadWithUserPermissionCheck(null, userId, articleId);
		return restTemplate.exchange(BY_ID_URL,
				HttpMethod.DELETE, he, SubContent.class, id).getBody();
	}

	@CircuitBreaker(name = SUBCONTENT_JOINED_SERVICE_CB, fallbackMethod = "removeOnError")
	public List<SubContent> removeByArticleId(Long articleId, Long userId)
	{
		var he = createPayloadWithUserPermissionCheck(null, userId, articleId);
		var ptr = new ParameterizedTypeReference<Catalogue<SubContent>>(){};
		return restTemplate.exchange(BY_ARTICLE_ID_URL,
				HttpMethod.DELETE, he, ptr, articleId).getBody().getList();
	}

	private HttpEntity<SubContent> createPayloadWithUserPermissionCheck(
			SubContent body, Long userId, Long articleId)
	{
		var safeArticleId = articleId != null ? articleId : -1;
		var safeUserId = userId != null ? userId : -1;
		getArticleByIdAndUserIdWithError(safeArticleId, safeUserId);
		var hh = HttpUtil.createDefaultHeaders();
		hh.setBasicAuth("system", "infopedia_system_1234");
		var he = new HttpEntity<SubContent>(body, hh);
		return he;
	}

	public Article getArticleByIdAndUserIdWithError(Long articleId, Long userId)
	{
		var article = articleService.getByIdAndUserId(articleId, userId);
		var foundId = article.getId();

		if(foundId != null && foundId > -1)
			return article;
		
		if(foundId < 0)
		{
			var message = "The article service is unavailable!";
			throw new RuntimeException(message);
		}

		var message = "The article was not found or the user is not the owner of the article!";
		throw new RuntimeException(message);
	}

	@SuppressWarnings("unused")
	private SubContent addOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}

	@SuppressWarnings("unused")
	private SubContent updateOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}

	@SuppressWarnings("unused")
	private SubContent removeOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}


	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private SubContentService subContentService;

	@Autowired
	private ArticleService articleService;


  private static final String SUBCONTENT_JOINED_SERVICE_CB = "subContentJoinedServiceCB";

	private static final String SUBCONTENT_URL =
			"http://infopedia-sub-content-service/api/sub-content";
	private static final String BY_ID_URL = SUBCONTENT_URL + "/{id}";
	private static final String BY_ARTICLE_ID_URL =
			SUBCONTENT_URL + "/by-article-id/{articleId}";
}
