package com.iorix2k2.infopedia.integration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iorix2k2.infopedia.integration.model.Article;
import com.iorix2k2.infopedia.integration.model.Catalogue;
import com.iorix2k2.infopedia.integration.model.SubContent;
import com.iorix2k2.infopedia.util.HttpUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


@Service
public class ArticleService
{
	@HystrixCommand(fallbackMethod = "getByIdOnError",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
			},
			threadPoolKey = "articleGetById",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public Article getById(Long id)
	{
		Article article = restTemplate.getForObject(byIdURL, Article.class, id);
		article.setUser(userService.getReducedById(article.getUserId()));
		article.setSubContentList(subContentService.getByArticleId(id));
		return article;
	}

	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
			},
			threadPoolKey = "articleGetByUserId",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public List<Article> getByUserId(Long userId)
	{
		return restTemplate.exchange(byUserIdURL, HttpMethod.GET, null,
				new ParameterizedTypeReference<Catalogue<Article>>(){}, userId).getBody().getList();
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
			},
			threadPoolKey = "articleGetByIdAndUserId",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public Article getByIdAndUserId(Long id, Long userId)
	{
		return restTemplate.getForObject(byIdAndUserIdURL, Article.class, id, userId);
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
			},
			threadPoolKey = "articleAddFromUser",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public Article addFromUser(Article article, Long userId)
	{
		article.setUserId(userId);
		HttpEntity<Article> he = createPayload(article);
		return restTemplate.exchange(articleURL, HttpMethod.POST, he,
				Article.class).getBody();
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
			},
			threadPoolKey = "articleUpdateFromUser",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public Article updateFromUser(Article article, Long userId)
	{
		article.setUserId(null);
		restTemplate.getForObject(byIdAndUserIdURL, Article.class,
				article.getId(), userId);
		HttpEntity<Article> he = createPayload(article);
		return restTemplate.exchange(byIdURL, HttpMethod.PATCH, he,
				Article.class, article.getId()).getBody();
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
			},
			threadPoolKey = "articleRemoveFromUser",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public Article remove(Long id)
	{
		Article articleFound = restTemplate.getForObject(byIdURL, Article.class, id);
		return removeInCascade(articleFound);
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
			},
			threadPoolKey = "articleRemoveFromUser",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public Article removeFromUser(Long id, Long userId)
	{
		Article articleFound = restTemplate.getForObject(byIdAndUserIdURL,
				Article.class, id, userId);
		return removeInCascade(articleFound);
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
			},
			threadPoolKey = "articleRemoveByUserId",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public List<Article> removeByUserId(Long userId)
	{
		List<SubContent> dscl = subContentService.removeByUserId(userId);
		HttpEntity<Article> he = createPayload(null);
		List<Article> dal = restTemplate.exchange(byUserIdURL, HttpMethod.DELETE,
				he, new ParameterizedTypeReference<Catalogue<Article>>(){}, userId).
				getBody().getList();
		Map<Long, Article> am = new HashMap<>();
		dal.forEach((article) -> am.put(article.getId(), article));
		dscl.forEach((subContent) -> 
		{
			Article a = am.get(subContent.getArticleId());
			
			if(a != null)
				a.getSubContentList().add(subContent);
		});
		
		return dal;
	}
	
	private Article removeInCascade(Article article)
	{
		List<SubContent> dscl = subContentService.removeByArticleId(
				article.getId(), article.getUserId());
		HttpEntity<Article> he = createPayload(null);
		Article deletedArticle = restTemplate.exchange(byIdURL, HttpMethod.DELETE,
				he, Article.class, article.getId()).getBody();
		deletedArticle.setSubContentList(dscl);
		return deletedArticle;
	}
	
	private HttpEntity<Article> createPayload(Article body)
	{
		HttpHeaders hh = HttpUtil.createDefaultHeaders();
		hh.setBasicAuth("system-admin", "infopedia_admin");
		HttpEntity<Article> he = new HttpEntity<>(body, hh);
		return he;
	}	

	@SuppressWarnings("unused")
	private Article getByIdOnError(Long id)
	{
		return getOnError(-1L);
	}
	
	private Article getOnError(Long id)
	{
		Article article = new Article();
		article.setId(id);
		article.setUserId(-1L);
		article.setTitle("Unavailable");
		article.setContent("Unavailable");
		return article;
	}	

	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SubContentService subContentService;
	

	private String articleURL = "http://infopedia-article-service/api/article";
	private String byIdURL = articleURL + "/{id}";
	private String byUserIdURL = articleURL + "/by-user-id/{userId}";
	private String byIdAndUserIdURL = articleURL + "/by-id-and-user-id/{id}/{userId}";
}
