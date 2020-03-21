package com.iorix2k2.infopedia.integration.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iorix2k2.infopedia.integration.model.Article;
import com.iorix2k2.infopedia.integration.model.User;
import com.iorix2k2.infopedia.integration.service.ArticleService;
import com.iorix2k2.infopedia.integration.service.UserService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/article")
public class ArticleController
{
	@GetMapping("/{id}")
	public ResponseEntity<Article> getById(@PathVariable Long id)
	{
		Article body = articleService.getById(id);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PostMapping("/from-user")
	public ResponseEntity<Article> addFromUser(@RequestBody Article article,
			HttpServletRequest request)
	{
		User user = checkTokenOrFullPermission(request);
		Article body = articleService.addFromUser(article, user.getId());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/from-user/{id}")
	public ResponseEntity<Article> updateFromUser(@PathVariable Long id,
			@RequestBody Article article, HttpServletRequest request)
	{
		article.setId(id);
		User user = checkTokenOrFullPermission(request);
		Article body = articleService.updateFromUser(article, user.getId());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Article> remove(@PathVariable Long id, 
			HttpServletRequest request)
	{
		HttpHeaders headers = HttpUtil.getHeadersFromRequest(request);
		userService.checkAdminPermission(headers);
		Article body = articleService.remove(id);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/from-user/{id}")
	public ResponseEntity<Article> removeFromUser(@PathVariable Long id, 
			HttpServletRequest request)
	{
		User user = checkTokenOrFullPermission(request);
		Article body = articleService.removeFromUser(id, user.getId());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private User checkTokenOrFullPermission(HttpServletRequest request)
	{
		HttpHeaders headers = HttpUtil.getHeadersFromRequest(request);
		return userService.checkTokenOrFullPermission(headers);
	}
	
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UserService userService;
}
