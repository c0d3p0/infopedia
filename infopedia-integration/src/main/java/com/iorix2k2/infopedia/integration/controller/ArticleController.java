package com.iorix2k2.infopedia.integration.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.iorix2k2.infopedia.integration.model.Catalogue;
import com.iorix2k2.infopedia.integration.model.User;
import com.iorix2k2.infopedia.integration.service.ArticleJoinedService;
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
		var body = articleJoinedService.getById(id);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@GetMapping("/by-sub-content-with/{content}")
	public ResponseEntity<Catalogue<Article>> getBySubContentWith(@PathVariable String content)
	{
		var body = new Catalogue<>(articleJoinedService.getBySubContentWith(content));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PostMapping("/by-user")
	public ResponseEntity<Article> addByUser(
			@RequestBody Article article, HttpServletRequest request)
	{
		var user = checkTokenOrFullPermission(request);
		var body = articleService.addByUser(user.getId(), article);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/by-user/{id}")
	public ResponseEntity<Article> updateByUser(@PathVariable Long id,
			@RequestBody Article article, HttpServletRequest request)
	{
		article.setId(id);
		var user = checkTokenOrFullPermission(request);
		var body = articleService.updateByUser(user.getId(), article);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Article> remove(
			@PathVariable Long id, HttpServletRequest request)
	{
		var headers = HttpUtil.getHeadersFromRequest(request);
		userService.checkAdminPermission(headers);
		var body = articleJoinedService.remove(id);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/by-user/{id}")
	public ResponseEntity<Article> removeByUser(
			@PathVariable Long id, HttpServletRequest request)
	{
		var user = checkTokenOrFullPermission(request);
		var body = articleJoinedService.removeByUser(id, user.getId());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private User checkTokenOrFullPermission(HttpServletRequest request)
	{
		var headers = HttpUtil.getHeadersFromRequest(request);
		return userService.checkTokenOrFullPermission(headers);
	}
	
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ArticleJoinedService articleJoinedService;
	
	@Autowired
	private UserService userService;
}
