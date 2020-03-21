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

import com.iorix2k2.infopedia.user.exception.InvalidDataException;
import com.iorix2k2.infopedia.user.exception.InvalidDataExceptionType;
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
	
	public List<User> getReducedRandom(Long amount)
	{
		List<Object[]> listFields = userRepository.findReducedTopAmountOrderByRandom(amount);
		List<User> listUser = new ArrayList<>();
		listFields.forEach((objs) -> listUser.add(new User(((BigInteger) objs[0]).longValue(),
				(String) objs[1], (Integer) objs[2], Gender.values()[(Integer) objs[3]],
				(String) objs[4])));
		return listUser;
	}
	
	public List<User> getReducedByUsernameWith(String username)
	{
		return userRepository.findReducedByUsernameContainingIgnoreCase(username);
	}
	
	public Optional<User> getReducedById(Long id)
	{
		return userRepository.findReducedById(id);
	}
	
	public Optional<User> getYourDataById(Long id)
	{
		return userRepository.findDataById(id);
	}	
	
	public Optional<User> findByUserAndPassword(String user, String password)
	{
		Optional<User> o = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(
				user, user);
		
		if(!o.isEmpty() &&
				passwordEncoder.matches(password != null ? password : "", o.get().getPassword()))
		{
			return o;
		}
		
		return Optional.empty();
	}
	
	public Optional<User> checkToken(String user, String token)
	{
		Optional<User> o = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(
				user, user);
		
		if(!o.isEmpty())
		{
			User userFound = o.get();
			
			if(passwordEncoder.matches(token, userFound.getToken()) &&
					userFound.getTokenActiveTime() > getNowInMilliseconds())
			{
				return o;
			}
			else
			{
				throw new InvalidDataException(InvalidDataExceptionType.
						UNMATCHED_EXPIRED_UNAUTHORIZED_DATA, "Invalid or expired token!");
			}
		}

		return Optional.empty();
	}
	
	public User add(User user)
	{
		user.setId(null);
		String token = createToken(user);
		validateFields(user, false);
		validateUniqueFields(user);
		fixPassword(user);
		User newUser = userRepository.save(user);
		newUser.setToken(token);
		return newUser;
	}
	
	public Optional<User> update(User user)
	{
		validateFields(user, true);
		validateUniqueFields(user);
		Optional<User> optional = userRepository.findById(user.getId());

		if(!optional.isEmpty())
		{
			user.setId(null);
			user.setToken(null);
			fixPassword(user);
			User updatedUser = optional.get();
			copyNonNull(user, updatedUser);
			updatedUser = userRepository.save(updatedUser);
			return Optional.of(updatedUser);
		}
		
		return Optional.empty();
	}
	
	public Optional<User> changePassword(Long id, String newPassword)
	{
		validateFields(new User(id, StringUtils.defaultString(newPassword)), true);
		Optional<User> o = userRepository.findById(id);
		
		if(!o.isEmpty())
		{
			User user = o.get();
			user.setPassword(passwordEncoder.encode(newPassword));
			User updatedUser = userRepository.save(user);
			return Optional.of(updatedUser);
		}
		
		return Optional.empty();
	}
	
	public Optional<User> generateNewToken(Long id)
	{
		Optional<User> o = userRepository.findById(id);
		
		if(!o.isEmpty())
		{
			User user = o.get();
			String token = createToken(user);
			User updatedUser = userRepository.save(user);
			updatedUser.setToken(token);
			return Optional.of(updatedUser);
		}
		
		return Optional.empty();
	}

	public Optional<User> expireToken(Long id)
	{
		Optional<User> o = userRepository.findById(id);
		
		if(!o.isEmpty())
		{
			User user = o.get();
			user.setTokenActiveTime(getNowInMilliseconds());
			User updatedUser = userRepository.save(user);
			return Optional.of(updatedUser);
		}
		
		return Optional.empty();
	}
	
	public Optional<User> remove(Long id)
	{
		Optional<User> optional = userRepository.findReducedById(id);
		
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
	
	public void cleanFullNameEmailPasswordToken(User user)
	{
		user.setFullName(null);
		user.setEmail(null);
		user.setPassword(null);
		user.setToken(null);
	}
	
	public void cleanCredentials(User user)
	{
		user.setPassword(null);
		user.setToken(null);
	}
	
	private void validateFields(User user, boolean ignoreNull)
	{
		String[] fields = {"fullName", "age", "gender", "country",
				"email", "username", "password"};
		boolean[] nullFields = {user.getFullName() == null, user.getAge() == null,
				user.getGender() == null, user.getCountry() == null, user.getEmail() == null,
				user.getUsername() == null, user.getPassword() == null};
		
		for(int i = 0; i < fields.length; i++)
		{
			if(ignoreNull && nullFields[i])
				continue;
			
			validator.validateProperty(user, fields[i]).forEach((violation) ->
			{
				throw new InvalidDataException(
						InvalidDataExceptionType.CONSTRAINT_NOT_SATISFIED, violation.getMessage());
			});
		}
  }
	
	private void validateUniqueFields(User user)
	{
		long id = user.getId() != null ? user.getId() : -1L;
		List<User> userList = userRepository.findByIdNotAndUsernameOrEmail(
				id, user.getUsername(), user.getEmail());
		
		if(userList.size() > 0)
		{
			User userFound = userList.get(0);
			
			if(!StringUtils.isBlank(user.getUsername()) &&
					userFound.getUsername().equals(user.getUsername()))
			{
				throw new InvalidDataException(InvalidDataExceptionType.DUPLICATED_DATA,
						"Username already registered in the system!");
			}
			
			if(!StringUtils.isBlank(user.getEmail()) && 
					userFound.getEmail().equals(user.getEmail()))
			{
				throw new InvalidDataException(InvalidDataExceptionType.DUPLICATED_DATA,
						"Email already registered in the system!");
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
		}
	}

	private String createToken(User user)
	{
		String token = UUID.randomUUID().toString();
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
