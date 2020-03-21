package com.iorix2k2.infopedia.user.controller;

import java.util.Map;
import java.util.Optional;

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
		Catalogue<User> body = new Catalogue<>(userService.getAll());
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable Long id)
	{			
		Optional<User> o = userService.getById(id);
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/data-reduced-random/{amount}")
	public ResponseEntity<Catalogue<User>> getReducedRandom(@PathVariable Long amount)
	{
		Catalogue<User> body = new Catalogue<>(userService.getReducedRandom(amount));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/data-reduced-by-username-with/{username}")
	public ResponseEntity<Catalogue<User>> getReducedByUsernameWith(
			@PathVariable String username)
	{
		Catalogue<User> body = new Catalogue<>(
				userService.getReducedByUsernameWith(username));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/data-reduced-by-id/{id}")
	public ResponseEntity<User> getReducedById(@PathVariable Long id)
	{
		Optional<User> o = userService.getReducedById(id);
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/your-data/{id}")
	public ResponseEntity<User> getYourDataById(@PathVariable Long id,
			Authentication authentication)
	{
		User user = (User) authentication.getPrincipal();
		Optional<User> o = userService.getYourDataById(user.getId());
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/check-full-permission")
	public ResponseEntity<User> checkFullPermission(Authentication authentication)
	{	
		User body = (User) authentication.getPrincipal();
		userService.cleanFullNameEmailPasswordToken(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/check-token-or-full-permission")
	public ResponseEntity<User> checkTokenOrFullPermission(Authentication authentication)
	{	
		User body = (User) authentication.getPrincipal();
		userService.cleanFullNameEmailPasswordToken(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@GetMapping("/check-admin-permission")
	public ResponseEntity<User> checkAdminPermission(Authentication authentication)
	{	
		User body = (User) authentication.getPrincipal();
		userService.cleanFullNameEmailPasswordToken(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/check-token")
	public ResponseEntity<User> checkToken(HttpServletRequest request)
	{	
		Map<String, String> map = HttpUtil.getBasicAuthCredentials(request, "IPU-Credentials");
		String user = map.get("username");
		String token = map.get("password");
		Optional<User> o = userService.checkToken(user, token);
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanFullNameEmailPasswordToken(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("")
	public ResponseEntity<User> add(@RequestBody User user, HttpServletRequest request)
	{
		setUserCredentialsFromHeaders(request, user);
		User body = userService.add(user);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/sign-up")
	public ResponseEntity<User> signUp(@RequestBody User user, HttpServletRequest request)
	{
		setUserCredentialsFromHeaders(request, user);
		User body = userService.add(user);
		userService.cleanFullNameEmailPassword(user);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/generate-token")
	public ResponseEntity<User> generateToken(Authentication authentication)
	{
		User user = (User) authentication.getPrincipal();
		Optional<User> o = userService.generateNewToken(user.getId());
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanFullNameEmailPassword(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/expire-token")
	public ResponseEntity<User> expireToken(Authentication authentication)
	{
		User user = (User) authentication.getPrincipal();
		Optional<User> o = userService.expireToken(user.getId());
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanFullNameEmailPasswordToken(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable Long id,
			@RequestBody User user, HttpServletRequest request)
	{
		setUserCredentialsFromHeaders(request, user);
		user.setId(id);
		Optional<User> o = userService.update(user);
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PatchMapping("/your-data/{id}")
	public ResponseEntity<User> updateYourData(@PathVariable Long id,
			@RequestBody User user, Authentication authentication)
	{
		User authorizedUser = (User) authentication.getPrincipal();
		user.setId(authorizedUser.getId());
		user.setPassword(null);
		user.setTokenActiveTime(null);
		Optional<User> o = userService.update(user);
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanCredentials(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}

	@PatchMapping("/change-password/{id}")
	public ResponseEntity<User> changePassword(@PathVariable Long id,
			HttpServletRequest request, Authentication authentication)
	{
		User user = (User) authentication.getPrincipal();
		Map<String, String> map = HttpUtil.getBasicAuthCredentials(request, "IPU-Credentials");
		String newPassword = map.get("password");
		Optional<User> o = userService.changePassword(user.getId(), newPassword);
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		userService.cleanFullNameEmailPasswordToken(body);
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<User> remove(@PathVariable Long id)
	{
		Optional<User> o = userService.remove(id);
		User body = o.orElseThrow(() -> createException("User not found in the system!"));
		return new ResponseEntity<>(body, HttpUtil.createDefaultHeaders(), HttpStatus.OK);
	}
	
	private void setUserCredentialsFromHeaders(HttpServletRequest request, User user)
	{
		user.setUsername(null);
		user.setPassword(null);
		Map<String, String> map = HttpUtil.getBasicAuthCredentials(request, "IPU-Credentials");
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
