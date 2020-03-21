package com.iorix2k2.infopedia.integration.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iorix2k2.infopedia.integration.model.User;
import com.iorix2k2.infopedia.integration.service.UserService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/user")
public class UserController
{
	@GetMapping("/check-admin-permission")
	public ResponseEntity<User> checkAdminPersmission(HttpServletRequest request)
	{
		User body = checkAdminPermission(request);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<User> remove(@PathVariable Long id, 
			HttpServletRequest request)
	{
		checkAdminPermission(request);
		User body = userService.remove(id);
		return new ResponseEntity<User>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/from-user/{id}")
	public ResponseEntity<User> removeFromUser(@PathVariable Long id, 
			HttpServletRequest request)
	{
		HttpHeaders headers = HttpUtil.getHeadersFromRequest(request);
		User user = userService.checkFullPermission(headers);
		User body = userService.remove(user.getId());
		return new ResponseEntity<User>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private User checkAdminPermission(HttpServletRequest request)
	{
		HttpHeaders headers = HttpUtil.getHeadersFromRequest(request);
		return userService.checkAdminPermission(headers);
	}
	
	
	@Autowired
	private UserService userService;
}
