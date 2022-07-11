package com.iorix2k2.infopedia.user.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.iorix2k2.infopedia.user.model.Catalogue;
import com.iorix2k2.infopedia.user.model.User;
import com.iorix2k2.infopedia.user.service.UserService;
import com.iorix2k2.infopedia.util.HttpUtil;


@RestController
@RequestMapping("/user")
public class UserController
{
	@GetMapping("")
	public ResponseEntity<Catalogue<User>> getAll()
	{
		var body = new Catalogue<>(userService.getAll());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable Long id)
	{			
		var o = userService.getById(id);
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@GetMapping("/by-username-or-email-with/{text}")
	public ResponseEntity<Catalogue<User>> getByUsernameOrEmail(@PathVariable String text)
	{			
		var body = new Catalogue<>(userService.getByUsernameOrEmailWith(text));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@GetMapping("/partial-random/{amount}")
	public ResponseEntity<Catalogue<User>> getPartialRandom(@PathVariable Long amount)
	{
		var body = new Catalogue<>(userService.getPartialRandom(amount));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/partial-by-username-with/{username}")
	public ResponseEntity<Catalogue<User>> getPartialByUsernameWith(
			@PathVariable String username)
	{
		var body = new Catalogue<>(userService.getPartialByUsernameWith(username));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/partial-by-id/{id}")
	public ResponseEntity<User> getPartialById(@PathVariable Long id)
	{
		var o = userService.getPartialById(id);
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/your-data")
	public ResponseEntity<User> getYourDataById(Authentication authentication)
	{
		var user = (User) authentication.getPrincipal();
		var o = userService.getYourDataById(user.getId());
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/check-full-permission")
	public ResponseEntity<User> checkFullPermission(Authentication authentication)
	{	
		var body = (User) authentication.getPrincipal();
		userService.cleanMainData(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/check-token-or-full-permission")
	public ResponseEntity<User> checkTokenOrFullPermission(Authentication authentication)
	{	
		var body = (User) authentication.getPrincipal();
		userService.cleanMainData(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@GetMapping("/check-admin-permission")
	public ResponseEntity<User> checkAdminPermission(Authentication authentication)
	{	
		var body = (User) authentication.getPrincipal();
		userService.cleanMainData(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/check-token")
	public ResponseEntity<User> checkToken(HttpServletRequest request)
	{	
		var map = HttpUtil.getBasicAuthCredentials(request, "IPU-Credentials");
		var user = map.get("username");
		var token = map.get("password");
		var o = userService.checkToken(user, token);
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanMainData(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("")
	public ResponseEntity<User> add(@RequestBody User user, HttpServletRequest request)
	{
		setUserCredentialsFromHeaders(request, user);
		var body = userService.add(user);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/sign-up")
	public ResponseEntity<User> signUp(@RequestBody User user, HttpServletRequest request)
	{
		setUserCredentialsFromHeaders(request, user);
		user.setSystemAdmin(false);
		var body = userService.add(user);
		userService.cleanFullNameEmailPassword(user);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/generate-token")
	public ResponseEntity<User> generateToken(Authentication authentication)
	{
		var user = (User) authentication.getPrincipal();
		var o = userService.generateNewToken(user.getId());
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanFullNameEmailPassword(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/expire-token")
	public ResponseEntity<User> expireToken(Authentication authentication)
	{
		var user = (User) authentication.getPrincipal();
		var o = userService.expireToken(user.getId());
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanMainData(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable Long id,
			@RequestBody User user, HttpServletRequest request)
	{
		setUserCredentialsFromHeaders(request, user);
		var un = user.getUsername();
		user.setUsername(un != null && !un.isBlank() ? un : null);
		user.setId(id);
		var o = userService.update(user);
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PatchMapping("/your-data")
	public ResponseEntity<User> updateYourData(
			@RequestBody User user, Authentication authentication)
	{
		var authorizedUser = (User) authentication.getPrincipal();
		user.setId(authorizedUser.getId());
		user.setPassword(null);
		user.setTokenActiveTime(null);
		user.setSystemAdmin(null);
		var o = userService.update(user);
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanCredentials(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<User> changePassword(
			HttpServletRequest request, Authentication authentication)
	{
		var user = (User) authentication.getPrincipal();
		var map = HttpUtil.getBasicAuthCredentials(request, "IPU-Credentials");
		var newPassword = map.get("password");
		var o = userService.changePassword(user.getId(), newPassword);
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanMainData(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<User> remove(@PathVariable Long id)
	{
		var o = userService.remove(id);
		var body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private void setUserCredentialsFromHeaders(HttpServletRequest request, User user)
	{
		user.setUsername(null);
		user.setPassword(null);
		var map = HttpUtil.getBasicAuthCredentials(request, "IPU-Credentials");
		user.setUsername(map.get("username"));
		user.setPassword(map.get("password"));
	}
	
	private ResponseStatusException createException(String message)
	{
		return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
	}
	
	
	@Autowired
	private UserService userService;
}
