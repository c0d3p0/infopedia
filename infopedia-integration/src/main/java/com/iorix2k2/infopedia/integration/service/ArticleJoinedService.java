package com.iorix2k2.infopedia.integration.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.iorix2k2.infopedia.integration.model.Article;
import com.iorix2k2.infopedia.integration.model.Catalogue;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


// Created to avoid circular dependencies.
@Service
public class ArticleJoinedService
{
	@CircuitBreaker(name = ARTICLE_JOINED_SERVICE_CB, fallbackMethod = "getMultipleOnError")
	public List<Article> getBySubContentWith(String content)
	{
		var scl = subContentService.getByContentWith(content);

		if(scl != null && scl.size() > 0)
		{
			var idSet = new HashSet<Long>();
			scl.forEach((subContent) -> {idSet.add(subContent.getArticleId());});
			var ids = idSet.toArray();
			var param = StringUtils.join(ids, ",");
			var ptr = new ParameterizedTypeReference<Catalogue<Article>>(){};
			return restTemplate.exchange(BY_IDS_URL,
					HttpMethod.GET, null, ptr, param).getBody().getList();
		}
		
		return new ArrayList<>();
	}

	@CircuitBreaker(name = ARTICLE_JOINED_SERVICE_CB, fallbackMethod = "getSingleOnError")
	public Article getById(Long id)
	{
		var article = restTemplate.getForObject(BY_ID_URL, Article.class, id);
		article.setUser(userService.getPartialById(article.getUserId()));
		article.setSubContentList(subContentService.getByArticleId(id));
		return article;
	}

	@CircuitBreaker(name = ARTICLE_JOINED_SERVICE_CB, fallbackMethod = "removeOnError")
	public Article remove(Long id)
	{
		var articleFound = restTemplate.getForObject(BY_ID_URL, Article.class, id);
		return removeInCascade(articleFound);
	}

	@CircuitBreaker(name = ARTICLE_JOINED_SERVICE_CB, fallbackMethod = "removeOnError")
	public Article removeByUser(Long id, Long userId)
	{
		var articleFound = restTemplate.getForObject(
				BY_ID_AND_USER_ID_URL, Article.class, id, userId);
		return removeInCascade(articleFound);
	}

	@CircuitBreaker(name = ARTICLE_JOINED_SERVICE_CB, fallbackMethod = "removeOnError")
	public List<Article> removeByUserId(Long userId)
	{
		var rscl = subContentService.removeByUserId(userId);
		var he = articleService.createPayload(null);
		var ptr = new ParameterizedTypeReference<Catalogue<Article>>(){};
		var dal = restTemplate.exchange(BY_USER_ID_URL,
				HttpMethod.DELETE, he, ptr, userId).getBody().getList();
		var articleMap = new HashMap<Long, Article>();
		dal.forEach((article) -> articleMap.put(article.getId(), article));
		rscl.forEach((subContent) -> 
		{
			var a = articleMap.get(subContent.getArticleId());
			
			if(a != null)
				a.getSubContentList().add(subContent);
		});

		return dal;
	}

	private Article removeInCascade(Article article)
	{
		var articleId = article.getId();
		var userId = article.getUserId();
		var rscl = subContentJoinedService.removeByArticleId(articleId, userId);
		var he = articleService.createPayload(null);
		var deletedArticle = restTemplate.exchange(BY_ID_URL,
				HttpMethod.DELETE, he, Article.class, article.getId()).getBody();
		deletedArticle.setSubContentList(rscl);
		return deletedArticle;
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
	private Article removeOnError(Throwable t)
	{
		if(t instanceof ResponseStatusException)
			throw (ResponseStatusException) t;

		throw new RuntimeException(t);
	}

	private Article getUnavailable(Long id, Long userId)
	{
		var article = articleService.getUnavailable(id, userId);
		var user = userService.getUnavailable(userId);
		article.setUser(user);
		return article;
	}


	@Autowired
	private UserService userService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private SubContentService subContentService;

	@Autowired
	private SubContentJoinedService subContentJoinedService;

	@Autowired
	private RestTemplate restTemplate;


	private static final String ARTICLE_JOINED_SERVICE_CB = "articleJoinedServiceCB";

	private static final String ARTICLE_URL =
			"http://infopedia-article-service/api/article";
	private static final String BY_ID_URL = ARTICLE_URL + "/{id}";
	private static final String BY_IDS_URL = ARTICLE_URL + "/by-ids/{ids}";
	private static final String BY_USER_ID_URL = ARTICLE_URL + "/by-user-id/{userId}";
	private static final String BY_ID_AND_USER_ID_URL =
			ARTICLE_URL + "/by-id-and-user-id/{id}/{userId}";
}
