package com.iorix2k2.infopedia.user.service;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iorix2k2.infopedia.user.error.InvalidDataException;
import com.iorix2k2.infopedia.user.error.InvalidDataExceptionType;
import com.iorix2k2.infopedia.user.model.Gender;
import com.iorix2k2.infopedia.user.model.User;
import com.iorix2k2.infopedia.user.repository.UserRepository;


@Service
public class UserService
{	
	public List<User> getAll()
	{
		return userRepository.findAll();
	}
	
	public Optional<User> getById(Long id)
	{
		return userRepository.findById(id);
	}

	public List<User> getByUsernameOrEmailWith(String username)
	{
		return userRepository.
				findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(username, username);
	}

	public List<User> getPartialRandom(Long amount)
	{
		var listFields = userRepository.findPartialTopAmountOrderByRandom(amount);
		var listUser = new ArrayList<User>();
		listFields.forEach((objs) -> listUser.add(
				new User(((BigInteger) objs[0]).longValue(), (String) objs[1],
				(Integer) objs[2], Gender.values()[(Integer) objs[3]],
				(String) objs[4])));
		return listUser;
	}
	
	public List<User> getPartialByUsernameWith(String username)
	{
		return userRepository.findPartialByUsernameContainingIgnoreCase(username);
	}
	
	public Optional<User> getPartialById(Long id)
	{
		return userRepository.findPartialById(id);
	}
	
	public Optional<User> getYourDataById(Long id)
	{
		return userRepository.findDataById(id);
	}	
	
	public Optional<User> getByUserAndPassword(String user, String password)
	{
		var o = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(user, user);
		var rawPassword = password != null ? password : "";
		var validation = !o.isEmpty() &&
				passwordEncoder.matches(rawPassword, o.get().getPassword());
		return validation ? o : Optional.empty();
	}
	
	public Optional<User> checkToken(String user, String token)
	{
		var o = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(user, user);
		
		if(!o.isEmpty())
		{
			var userFound = o.get();
			var validToken = passwordEncoder.matches(token, userFound.getToken());
			var validTime = userFound.getTokenActiveTime() > getNowInMilliseconds();
			
			if(validToken && validTime)
				return o;

			var t = InvalidDataExceptionType.UNMATCHED_EXPIRED_UNAUTHORIZED_DATA;
			throw new InvalidDataException(t, "Invalid or expired token!");
		}

		return Optional.empty();
	}
	
	public User add(User user)
	{
		user.setId(null);
		var token = createToken(user);
		validateFields(user, false);
		validateUniqueFields(user);
		fixPassword(user);
		var newUser = userRepository.save(user);
		newUser.setToken(token);
		return newUser;
	}
	
	public Optional<User> update(User user)
	{
		validateFields(user, true);
		validateUniqueFields(user);
		var optional = userRepository.findById(user.getId());

		if(!optional.isEmpty())
		{
			user.setId(null);
			user.setToken(null);
			user.setTokenActiveTime(null);
			fixPassword(user);
			var updatedUser = optional.get();
			copyNonNull(user, updatedUser);
			updatedUser = userRepository.save(updatedUser);
			return Optional.of(updatedUser);
		}
		
		return Optional.empty();
	}
	
	public Optional<User> changePassword(Long id, String newPassword)
	{
		validateFields(new User(id, StringUtils.defaultString(newPassword)), true);
		var o = userRepository.findById(id);
		
		if(!o.isEmpty())
		{
			var user = o.get();
			user.setPassword(passwordEncoder.encode(newPassword));
			var updatedUser = userRepository.save(user);
			return Optional.of(updatedUser);
		}
		
		return Optional.empty();
	}
	
	public Optional<User> generateNewToken(Long id)
	{
		var o = userRepository.findById(id);
		
		if(!o.isEmpty())
		{
			var user = o.get();
			var token = createToken(user);
			var updatedUser = userRepository.save(user);
			updatedUser.setToken(token);
			return Optional.of(updatedUser);
		}
		
		return Optional.empty();
	}

	public Optional<User> expireToken(Long id)
	{
		var o = userRepository.findById(id);
		
		if(!o.isEmpty())
		{
			var user = o.get();
			user.setTokenActiveTime(getNowInMilliseconds());
			var updatedUser = userRepository.save(user);
			return Optional.of(updatedUser);
		}
		
		return Optional.empty();
	}
	
	public Optional<User> remove(Long id)
	{
		var optional = userRepository.findPartialById(id);
		
		if(!optional.isEmpty())
			userRepository.deleteById(id);
		
		return optional;
	}
	
	public Optional<User> remove(User user)
	{
		return remove(user.getId());
	}
	
	public void cleanFullNameEmailPassword(User user)
	{
		user.setFullName(null);
		user.setEmail(null);
		user.setPassword(null);
	}
	
	public void cleanMainData(User user)
	{
		user.setFullName(null);
		user.setEmail(null);
		user.setPassword(null);
		user.setToken(null);
		user.setTokenActiveTime(null);
		user.setSystemAdmin(null);
	}
	
	public void cleanCredentials(User user)
	{
		user.setPassword(null);
		user.setToken(null);
	}
	
	private void validateFields(User user, boolean ignoreNull)
	{
		var fields = new String[]{"fullName", "age",
				"gender", "country", "email", "username", "password"};
		var nullFields = new boolean[]{user.getFullName() == null,
				user.getAge() == null, user.getGender() == null, user.getCountry() == null,
				user.getEmail() == null, user.getUsername() == null, user.getPassword() == null};
		
		for(int i = 0; i < fields.length; i++)
		{
			if(ignoreNull && nullFields[i])
				continue;
			
			validator.validateProperty(user, fields[i]).forEach((violation) ->
			{
				var t = InvalidDataExceptionType.CONSTRAINT_NOT_SATISFIED;
				throw new InvalidDataException(t, violation.getMessage());
			});
		}
	}
	
	private void validateUniqueFields(User user)
	{
		var id = user.getId() != null ? user.getId() : -1L;
		var userList = userRepository.findByIdNotAndUsernameOrEmail(
				id, user.getUsername(), user.getEmail());
		
		if(userList.size() > 0)
		{
			var userFound = userList.get(0);
			
			if(!StringUtils.isBlank(user.getUsername()) &&
					userFound.getUsername().equals(user.getUsername()))
			{
				var t = InvalidDataExceptionType.DUPLICATED_DATA;
				throw new InvalidDataException(t, "Username already registered in the system!");
			}
			
			if(!StringUtils.isBlank(user.getEmail()) && 
					userFound.getEmail().equals(user.getEmail()))
			{
				var t = InvalidDataExceptionType.DUPLICATED_DATA;
				throw new InvalidDataException(t, "Email already registered in the system!");
			}
		}
		
	}
	
	private void copyNonNull(User from, User to)
	{
		if(from != null)
		{
			if(from.getId() != null)
				to.setId(from.getId());
			
			if(from.getFullName() != null)
				to.setFullName(from.getFullName());
			
			if(from.getAge() != null)
				to.setAge(from.getAge());
			
			if(from.getGender() != null)
				to.setGender(from.getGender());
			
			if(from.getCountry() != null)
				to.setCountry(from.getCountry());
			
			if(from.getEmail() != null)
				to.setEmail(from.getEmail());
			
			if(from.getUsername() != null)
				to.setUsername(from.getUsername());
			
			if(from.getPassword() != null)
				to.setPassword(from.getPassword());
			
			if(from.getTokenActiveTime() != null)
				to.setTokenActiveTime(from.getTokenActiveTime());

			if(from.isSystemAdmin() != null)
				to.setSystemAdmin(from.isSystemAdmin());
		}
	}

	private String createToken(User user)
	{
		var token = UUID.randomUUID().toString();
		user.setToken(passwordEncoder.encode(token));
		user.setTokenActiveTime(getNowInMilliseconds() + 7_200_000L);
		return token;
	}
	
	private void fixPassword(User user)
	{
		if(user.getPassword() != null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));
	}
	
	private long getNowInMilliseconds()
	{
		return ZonedDateTime.now().toInstant().toEpochMilli();
	}

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private Validator validator;
}
