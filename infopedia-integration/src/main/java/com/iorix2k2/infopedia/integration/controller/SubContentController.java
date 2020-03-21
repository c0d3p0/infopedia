package com.iorix2k2.infopedia.integration.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iorix2k2.infopedia.integration.model.SubContent;
import com.iorix2k2.infopedia.integration.model.User;
import com.iorix2k2.infopedia.integration.service.SubContentService;
import com.iorix2k2.infopedia.integration.service.UserService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/sub-content")
public class SubContentController
{
	@PostMapping("/from-user")
	public ResponseEntity<SubContent> addFromUser(@RequestBody SubContent subContent,
			HttpServletRequest request)
	{
		User user = checkTokenOrFullPermission(request);
		SubContent body = subContentService.addFromUser(subContent, user.getId());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/from-user/{id}")
	public ResponseEntity<SubContent> updateFromUser(@PathVariable Long id,
			@RequestBody SubContent subContent, HttpServletRequest request)
	{
		subContent.setId(id);
		User user = checkTokenOrFullPermission(request);
		SubContent body = subContentService.updateFromUser(subContent, user.getId());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/from-user/{id}")
	public ResponseEntity<SubContent> removeFromUser(@PathVariable Long id,
			HttpServletRequest request)
	{
		User user = checkTokenOrFullPermission(request);
		SubContent body = subContentService.removeFromUser(id, user.getId());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private User checkTokenOrFullPermission(HttpServletRequest request)
	{
		HttpHeaders headers = HttpUtil.getHeadersFromRequest(request);
		return userService.checkTokenOrFullPermission(headers);
	}
	
	
	@Autowired
	private SubContentService subContentService;
	
	@Autowired
	private UserService userService;
}
