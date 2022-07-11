package com.iorix2k2.infopedia.integration.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iorix2k2.infopedia.integration.model.User;
import com.iorix2k2.infopedia.integration.service.UserJoinedService;
import com.iorix2k2.infopedia.integration.service.UserService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/user")
public class UserController
{	
	@DeleteMapping("/{id}")
	public ResponseEntity<User> remove(@PathVariable Long id, HttpServletRequest request)
	{
		checkAdminPermission(request);
		var body = userJoinedService.remove(id);
		return new ResponseEntity<User>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/remove-account")
	public ResponseEntity<User> removeAccount(HttpServletRequest request)
	{
		var headers = HttpUtil.getHeadersFromRequest(request);
		var user = userService.checkFullPermission(headers);
		var body = userJoinedService.remove(user.getId());
		return new ResponseEntity<User>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private User checkAdminPermission(HttpServletRequest request)
	{
		var headers = HttpUtil.getHeadersFromRequest(request);
		return userService.checkAdminPermission(headers);
	}
	
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserJoinedService userJoinedService;
}
