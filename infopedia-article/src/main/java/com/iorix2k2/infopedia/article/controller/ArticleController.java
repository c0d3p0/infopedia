package com.iorix2k2.infopedia.article.controller;


import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import com.iorix2k2.infopedia.article.service.DispatchService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/article")
public class ArticleController
{
	@GetMapping("")
	public ResponseEntity<Catalogue<Article>> getAll()
	{
		Catalogue<Article> body = new Catalogue<>(articleService.getAll());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/random/{amount}")
	public ResponseEntity<Catalogue<Article>> getRandom(@PathVariable Long amount)
	{
		Catalogue<Article> body = new Catalogue<>(articleService.getRandom(amount));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/by-user-id/{userId}")
	public ResponseEntity<Catalogue<Article>> getByUserId(@PathVariable Long userId)
	{
		Catalogue<Article> body = new Catalogue<>(articleService.getByUserId(userId));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/by-title-with/{title}")
	public ResponseEntity<Catalogue<Article>> getByTitleWith(@PathVariable String title)
	{
		Catalogue<Article> body = new Catalogue<>(articleService.getByTitleWith(title));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Article> getById(@PathVariable Long id)
	{
		Optional<Article> o = articleService.getById(id);
		Article body = o.orElseThrow(() -> createException(
				"Article not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK); 
	}
	
	@GetMapping("/by-id-and-user-id/{id}/{userId}")
	public ResponseEntity<Article> getByIdAndUserId(@PathVariable Long id,
			@PathVariable Long userId)
	{
		Optional<Article> o = articleService.getByIdAndUserId(id, userId);
		Article body = o.orElseThrow(() -> createException(
				"Article not found in the system or user informed isn't the author of the article!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK); 
	}
	
	@PostMapping("")
	public ResponseEntity<Article> add(@RequestBody Article article,
			HttpServletRequest request)
	{
		checkAdminPermission(request);
		Article body = articleService.add(article);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Article> update(@PathVariable Long id,
			@RequestBody Article article, HttpServletRequest request)
	{
		article.setId(id);
		checkAdminPermission(request);
		Optional<Article> o = articleService.update(article);
		Article body = o.orElseThrow(() -> createException(
				"Article not found in the system!"));			
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Article> remove(@PathVariable Long id,
			HttpServletRequest request)
	{
		checkAdminPermission(request);
		Optional<Article> o = articleService.remove(id);
		Article body = o.orElseThrow(() -> createException(
				"Article not found in the system!"));			
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/by-user-id/{userId}")
	public ResponseEntity<Catalogue<Article>> removeByUserId(@PathVariable Long userId,
			HttpServletRequest request)
	{
		checkAdminPermission(request);
		Catalogue<Article> body = new Catalogue<>(articleService.removeByUserId(userId));		
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	private void checkAdminPermission(HttpServletRequest request)
	{
		HttpHeaders headers = HttpUtil.getHeadersFromRequest(request);
		dispatchService.dispatch(checkAdminPermissionURL, HttpMethod.GET, headers,
				null, String.class, null);
	}
	
	private ResponseStatusException createException(String message)
	{
		return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
	}
	
	
	@Autowired
	public ArticleService articleService;
	
	@Autowired
	private DispatchService dispatchService;
	
	private String checkAdminPermissionURL =
			"http://infopedia-integration-service/api/user/check-admin-permission";
}
