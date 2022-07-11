package com.iorix2k2.infopedia.article.controller;


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
import org.springframework.web.server.ResponseStatusException;

import com.iorix2k2.infopedia.article.model.Article;
import com.iorix2k2.infopedia.article.model.Catalogue;
import com.iorix2k2.infopedia.article.service.ArticleService;
import com.iorix2k2.infopedia.article.service.AuthenticationService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/article")
public class ArticleController
{
	@GetMapping("")
	public ResponseEntity<Catalogue<Article>> getAll()
	{
		var body = new Catalogue<>(articleService.getAll());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/random/{amount}")
	public ResponseEntity<Catalogue<Article>> getRandom(@PathVariable Long amount)
	{
		var body = new Catalogue<>(articleService.getRandom(amount));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/by-user-id/{userId}")
	public ResponseEntity<Catalogue<Article>> getByUserId(@PathVariable Long userId)
	{
		var body = new Catalogue<>(articleService.getByUserId(userId));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/by-title-with/{title}")
	public ResponseEntity<Catalogue<Article>> getByTitleWith(@PathVariable String title)
	{
		var body = new Catalogue<>(articleService.getByTitleWith(title));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/by-content-with/{content}")
	public ResponseEntity<Catalogue<Article>> getByContentWith(@PathVariable String content)
	{
		var body = new Catalogue<>(articleService.getByContentWith(content));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@GetMapping("/by-ids/{ids}")
	public ResponseEntity<Catalogue<Article>> getByIds(@PathVariable Long[] ids)
	{
		var body = new Catalogue<>(articleService.getByIds(ids));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Article> getById(@PathVariable Long id)
	{
		var o = articleService.getById(id);
		var body = o.orElseThrow(() -> createException("Article not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK); 
	}
	
	@GetMapping("/by-id-and-user-id/{id}/{userId}")
	public ResponseEntity<Article> getByIdAndUserId(@PathVariable Long id,
			@PathVariable Long userId)
	{
		var o = articleService.getByIdAndUserId(id, userId);
		var body = o.orElseThrow(() -> createException(
				"Article not found in the system or user informed isn't the author of the article!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK); 
	}
	
	@PostMapping("")
	public ResponseEntity<Article> add(
			@RequestBody Article article, HttpServletRequest request)
	{
		authenticationService.checkAdminPermission(request);
		var body = articleService.add(article);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Article> update(@PathVariable Long id,
			@RequestBody Article article, HttpServletRequest request)
	{
		article.setId(id);
		authenticationService.checkAdminPermission(request);
		var o = articleService.update(article);
		var body = o.orElseThrow(() -> createException("Article not found in the system!"));			
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Article> remove(
			@PathVariable Long id, HttpServletRequest request)
	{
		authenticationService.checkAdminPermission(request);
		var o = articleService.remove(id);
		var body = o.orElseThrow(() -> createException("Article not found in the system!"));			
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/by-user-id/{userId}")
	public ResponseEntity<Catalogue<Article>> removeByUserId(
			@PathVariable Long userId, HttpServletRequest request)
	{
		authenticationService.checkAdminPermission(request);
		var body = new Catalogue<>(articleService.removeByUserId(userId));		
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private ResponseStatusException createException(String message)
	{
		return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
	}
	
	
	@Autowired
	public ArticleService articleService;

	@Autowired
	public AuthenticationService authenticationService;
}
