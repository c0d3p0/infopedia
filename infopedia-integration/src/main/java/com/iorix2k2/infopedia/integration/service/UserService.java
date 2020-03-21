package com.iorix2k2.infopedia.integration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iorix2k2.infopedia.integration.model.Article;
import com.iorix2k2.infopedia.integration.model.User;
import com.iorix2k2.infopedia.util.HttpUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


@Service
public class UserService
{
	@HystrixCommand(fallbackMethod = "getReducedByUsernameWithOnError", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")}, 
			threadPoolKey = "userGetReducedById",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public User getReducedById(Long id)
	{
		return restTemplate.getForObject(reducedByIdURL, User.class, id);
	}

	@HystrixCommand(fallbackMethod = "", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")}, 
			threadPoolKey = "userRemoveAccount",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public User remove(Long id)
	{
		List<Article> dal = articleService.removeByUserId(id);
		HttpHeaders hh = HttpUtil.createDefaultHeaders();
		hh.setBasicAuth("system-admin", "infopedia_admin");
		HttpEntity<User> he = new HttpEntity<>(null, hh);
		User deletedUser = restTemplate.exchange(byIdURL, HttpMethod.DELETE,
				he, User.class, id).getBody();
		deletedUser.setArticleList(dal);
		return deletedUser;
	}
	
	@HystrixCommand(fallbackMethod = "", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")}, 
			threadPoolKey = "userCheckAdminPermission",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public User checkAdminPermission(HttpHeaders headers)
	{
		return dispatchService.dispatch(checkAdminPermissionURL,
				HttpMethod.GET, headers, null, User.class, null).getBody();
	}
	
	@HystrixCommand(fallbackMethod = "", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")}, 
			threadPoolKey = "userCheckFullPermission",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public User checkFullPermission(HttpHeaders headers)
	{
		return dispatchService.dispatch(checkFullPermissionURL,
				HttpMethod.GET, headers, null, User.class, null).getBody();
	}
	
	@HystrixCommand(fallbackMethod = "", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "8000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "40"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")}, 
			threadPoolKey = "userCheckTokenOrFullPermission",
			threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "100")})
	public User checkTokenOrFullPermission(HttpHeaders headers)
	{
		return dispatchService.dispatch(checkTokenOrFullPermissionURL,
				HttpMethod.GET, headers, null, User.class, null).getBody();
	}
	
	@SuppressWarnings("unused")
	private User getReducedByIdOnError(Long id)
	{
		return getOnError(id);
	}
	
	private User getOnError(Long id)
	{
		User user = new User();
		user.setId(id);
		user.setUsername("Unavailable");
		return user;
	}

	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DispatchService dispatchService;
	
	@Autowired
	private ArticleService articleService;
	
	
	
	private String userURL = "http://infopedia-user-service/api/user";
	private String reducedByIdURL = userURL + "/data-reduced-by-id/{id}";
	private String byIdURL = userURL + "/{id}";
	private String checkAdminPermissionURL = userURL + "/check-admin-permission";
	private String checkFullPermissionURL = userURL +
			"/check-full-permission";
	private String checkTokenOrFullPermissionURL = userURL +
			"/check-token-or-full-permission";
}
