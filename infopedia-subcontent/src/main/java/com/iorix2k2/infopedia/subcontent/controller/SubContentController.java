package com.iorix2k2.infopedia.subcontent.controller;


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

import com.iorix2k2.infopedia.subcontent.model.Catalogue;
import com.iorix2k2.infopedia.subcontent.model.SubContent;
import com.iorix2k2.infopedia.subcontent.service.AuthenticationService;
import com.iorix2k2.infopedia.subcontent.service.SubContentService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/sub-content")
public class SubContentController
{
	@GetMapping("")
	public ResponseEntity<Catalogue<SubContent>> getAll()
	{
		var body = new Catalogue<>(subContentService.getAll());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/by-article-id/{articleId}")
	public ResponseEntity<Catalogue<SubContent>> getByArticleId(@PathVariable Long articleId)
	{
		var body = new Catalogue<>(subContentService.getByArticleId(articleId));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SubContent> getById(@PathVariable Long id)
	{
		var optional = subContentService.getById(id);
		var body = optional.orElseThrow(() ->
				createException("Sub content not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@GetMapping("/by-content-with/{content}")
	public ResponseEntity<Catalogue<SubContent>> getByContentWith(@PathVariable String content)
	{
		var body = new Catalogue<>(subContentService.getByContentWith(content));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<SubContent> add(
			@RequestBody SubContent subContent, HttpServletRequest request)
	{
		authenticationService.checkAdminPermission(request);
		var body = subContentService.add(subContent);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<SubContent> update(@PathVariable Long id,
			@RequestBody SubContent subContent, HttpServletRequest request)
	{
		authenticationService.checkAdminPermission(request);
		subContent.setId(id);
		var optional = subContentService.update(subContent);
		var body = optional.orElseThrow(() ->
				createException("Sub content not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<SubContent> remove(
			@PathVariable Long id, HttpServletRequest request)
	{
		authenticationService.checkAdminPermission(request);
		var optional = subContentService.remove(id);
		var body = optional.orElseThrow(() ->
				createException("Sub content not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/by-article-id/{articleId}")
	public ResponseEntity<Catalogue<SubContent>> removeByArticleId(
			@PathVariable Long articleId, HttpServletRequest request)
	{
		authenticationService.checkAdminPermission(request);
		var removedList = subContentService.removeByArticleId(articleId);
		var body = new Catalogue<>(removedList);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/by-user-id/{userId}")
	public ResponseEntity<Catalogue<SubContent>> removeByUserId(
			@PathVariable Long userId, HttpServletRequest request)
	{
		authenticationService.checkAdminPermission(request);
		var body = new Catalogue<>(subContentService.removeByUserId(userId));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	private ResponseStatusException createException(String message)
	{
		return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
	}
	
	
	@Autowired
	private SubContentService subContentService;
	
	@Autowired
	private AuthenticationService authenticationService;
}
