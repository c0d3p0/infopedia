package com.iorix2k2.infopedia.integration.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iorix2k2.infopedia.integration.model.Catalogue;
import com.iorix2k2.infopedia.integration.model.SubContent;
import com.iorix2k2.infopedia.integration.model.SubContentType;
import com.iorix2k2.infopedia.util.HttpUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


@Service
public class SubContentService
{
	@HystrixCommand(fallbackMethod = "getByArticleIdOnError",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
			},
			threadPoolKey = "subContentGetByArticleId",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public List<SubContent> getByArticleId(Long articleId)
	{
		return restTemplate.exchange(byArticleIdURL, HttpMethod.GET, null,
				new ParameterizedTypeReference<Catalogue<SubContent>>(){}, articleId).getBody().getList();
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
			},
			threadPoolKey = "subContentAddFromUser",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public SubContent addFromUser(SubContent subContent, Long userId)
	{
		subContent.setUserId(userId);
		HttpEntity<SubContent> he = createPayloadWithUserPermissionCheck(
				subContent, userId, subContent.getArticleId());
		return restTemplate.exchange(subContentURL, HttpMethod.POST, he,
				SubContent.class).getBody();
	}

	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
			},
			threadPoolKey = "subContentUpdateFromUser",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public SubContent updateFromUser(SubContent subContent, Long userId)
	{
		subContent.setUserId(null);
		subContent.setArticleId(null);
		SubContent subContentFound = getById(subContent.getId());
		HttpEntity<SubContent> he = createPayloadWithUserPermissionCheck(
				subContent, userId, subContentFound.getArticleId());
		return restTemplate.exchange(byIdURL, HttpMethod.PATCH, he,
				SubContent.class, subContent.getId()).getBody();
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
			},
			threadPoolKey = "subContentRemoveFromUser",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public SubContent removeFromUser(Long id, Long userId)
	{
		SubContent subContentFound = getById(id);
		HttpEntity<SubContent> he = createPayloadWithUserPermissionCheck(
				null, userId, subContentFound.getArticleId());
		return restTemplate.exchange(byIdURL, HttpMethod.DELETE, he,
				SubContent.class, id).getBody();
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
			},
			threadPoolKey = "subContentRemoveByArticleId",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public List<SubContent> removeByArticleId(Long articleId, Long userId)
	{
		HttpEntity<SubContent> he = createPayloadWithUserPermissionCheck(
				null, userId, articleId);
		return restTemplate.exchange(byArticleIdURL, HttpMethod.DELETE, he,
				new ParameterizedTypeReference<Catalogue<SubContent>>(){}, articleId).
				getBody().getList();
	}
	
	@HystrixCommand(fallbackMethod = "",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
			},
			threadPoolKey = "subContentRemoveByUserId",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public List<SubContent> removeByUserId(Long userId)
	{
		HttpHeaders hh = HttpUtil.createDefaultHeaders();
		hh.setBasicAuth("system-admin", "infopedia_admin");
		HttpEntity<SubContent> he = new HttpEntity<>(null, hh);
		return restTemplate.exchange(byUserIdURL, HttpMethod.DELETE, he,
				new ParameterizedTypeReference<Catalogue<SubContent>>(){}, userId).
				getBody().getList();
	}
	
	private SubContent getById(Long id)
	{
		return restTemplate.exchange(byIdURL, HttpMethod.GET, null,
				SubContent.class, id).getBody();
	}
	
	private HttpEntity<SubContent> createPayloadWithUserPermissionCheck(SubContent body,
			Long userId, Long articleId)
	{
		articleService.getByIdAndUserId(articleId != null ? articleId : 0,
				userId != null ? userId : 0);
		HttpHeaders hh = HttpUtil.createDefaultHeaders();
		hh.setBasicAuth("system-admin", "infopedia_admin");
		HttpEntity<SubContent> he = new HttpEntity<>(body, hh);
		return he;
	}	
	
	@SuppressWarnings("unused")
	private List<SubContent> getByArticleIdOnError(Long articleId)
	{
		return Arrays.asList(getOnError(-1L, articleId));
	}
	
	private SubContent getOnError(Long id, Long articleId)
	{
		SubContent sc = new SubContent();
		sc.setId(id);
		sc.setArticleId(articleId);
		sc.setPosition((short)1);
		sc.setType(SubContentType.TEXT);
		sc.setTitle("Unavailable");
		sc.setContent("Content unavailable");
		return sc;
	}
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ArticleService articleService;
	
	
	private String subContentURL =
			"http://infopedia-sub-content-service/api/sub-content";
	private String byIdURL = subContentURL + "/{id}";
	private String byArticleIdURL = subContentURL + "/by-article-id/{articleId}";
	private String byUserIdURL = subContentURL + "/by-user-id/{userId}";
}
