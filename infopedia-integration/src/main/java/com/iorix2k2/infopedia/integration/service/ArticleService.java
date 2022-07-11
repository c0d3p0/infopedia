package com.iorix2k2.infopedia.integration.service;

import java.util.ArrayList;
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

import com.iorix2k2.infopedia.integration.model.Article;
import com.iorix2k2.infopedia.integration.model.Catalogue;
import com.iorix2k2.infopedia.integration.model.SubContent;
import com.iorix2k2.infopedia.util.HttpUtil;


@Service
public class ArticleService
{
	@CircuitBreaker(name = ARTICLE_SERVICE_CB, fallbackMethod = "getMultipleOnError")
	public List<Article> getByUserId(Long userId)
	{
		var ptr = new ParameterizedTypeReference<Catalogue<Article>>(){};
		return restTemplate.exchange(BY_USER_ID_URL,
				HttpMethod.GET, null, ptr, userId).getBody().getList();
	}

	@CircuitBreaker(name = ARTICLE_SERVICE_CB, fallbackMethod = "getSingleOnError")
	public Article getByIdAndUserId(Long id, Long userId)
	{
		return restTemplate.getForObject(
				BY_ID_AND_USER_ID_URL, Article.class, id, userId);
	}

	@CircuitBreaker(name = ARTICLE_SERVICE_CB, fallbackMethod = "addOnError")
	public Article addByUser(Long userId, Article article)
	{
		article.setUserId(userId);
		var he = createPayload(article);
		return restTemplate.exchange(ARTICLE_URL, HttpMethod.POST, he,
				Article.class).getBody();
	}

	@CircuitBreaker(name = ARTICLE_SERVICE_CB, fallbackMethod = "updateOnError")
	public Article updateByUser(Long userId, Article article)
	{
		article.setUserId(null);
		getByIdAndUserIdWithError(article.getId(), userId);
		var he = createPayload(article);
		return restTemplate.exchange(BY_ID_URL, HttpMethod.PATCH,
				he, Article.class, article.getId()).getBody();
	}

	public Article getByIdAndUserIdWithError(Long id, Long userId)
	{
		var article = getByIdAndUserId(id, userId);
		var articleId = article.getId();

		if(articleId != null && articleId > -1)
			return article;
		
		if(articleId < 0)
		{
			var message = "The article service is unavailable!";
			throw new RuntimeException(message);
		}

		var message = "Article not found or the user is not the owner of the article!";
		throw new RuntimeException(message);
	}

	public HttpEntity<Article> createPayload(Article body)
	{
		var hh = HttpUtil.createDefaultHeaders();
		hh.setBasicAuth("system", "infopedia_system_1234");
		var he = new HttpEntity<>(body, hh);
		return he;
	}

	@SuppressWarnings("unused")
	private List<Article> getMultipleOnError(Throwable t)
	{
		return Arrays.asList(getUnavailable(-1L, -1L));
	}

	@SuppressWarnings("unused")
	private Article getSingleOnError(Throwable t)
	{
		return getUnavailable(-1L, -1L);
	}

	@SuppressWarnings("unused")
	private Article addOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}

	@SuppressWarnings("unused")
	private Article updateOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}

	public Article getUnavailable(Long id, Long userId)
	{
		var article = new Article();
		article.setId(id);
		article.setContent("Unavailable");
		article.setSubContentList(new ArrayList<SubContent>());
		article.setTitle("Unavailable");
		article.setUserId(userId);
		return article;
	}


	@Autowired
	private RestTemplate restTemplate;


	private static final String ARTICLE_SERVICE_CB = "articleServiceCB";
	private static final String ARTICLE_URL =
			"http://infopedia-article-service/api/article";
	private static final String BY_ID_URL = ARTICLE_URL + "/{id}";
	private static final String BY_USER_ID_URL = ARTICLE_URL + "/by-user-id/{userId}";
	private static final String BY_ID_AND_USER_ID_URL =
			ARTICLE_URL + "/by-id-and-user-id/{id}/{userId}";
}
