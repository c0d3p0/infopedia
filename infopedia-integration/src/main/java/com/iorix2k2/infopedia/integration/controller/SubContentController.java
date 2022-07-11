package com.iorix2k2.infopedia.integration.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.iorix2k2.infopedia.integration.service.SubContentJoinedService;
import com.iorix2k2.infopedia.integration.service.UserService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/sub-content")
public class SubContentController
{
	@PostMapping("/by-user")
	public ResponseEntity<SubContent> addByUser(
			@RequestBody SubContent subContent, HttpServletRequest request)
	{
		var user = checkTokenOrFullPermission(request);
		var body = subContentJoinedService.addByUser(user.getId(), subContent);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/by-user/{id}")
	public ResponseEntity<SubContent> updateByUser(@PathVariable Long id,
			@RequestBody SubContent subContent, HttpServletRequest request)
	{
		subContent.setId(id);
		var user = checkTokenOrFullPermission(request);
		var body = subContentJoinedService.updateByUser(user.getId(), subContent);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/by-user/{id}")
	public ResponseEntity<SubContent> removeByUser(
			@PathVariable Long id, HttpServletRequest request)
	{
		var user = checkTokenOrFullPermission(request);
		var body = subContentJoinedService.removeByUser(id, user.getId());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private User checkTokenOrFullPermission(HttpServletRequest request)
	{
		var headers = HttpUtil.getHeadersFromRequest(request);
		return userService.checkTokenOrFullPermission(headers);
	}


	@Autowired
	private SubContentJoinedService subContentJoinedService;

	@Autowired
	private UserService userService;
}
