package com.iorix2k2.infopedia.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "infopedia_user")
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
public class User
{	
	public User() {}
	
	public User(Long id, String password)
	{
		setId(id);
		setPassword(password);
	}
	
	public User(Long id, String username, String email)
	{
		setId(id);
		setUsername(username);
		setEmail(email);
	}
	
	public User(Long id, String username, Integer age, Gender gender, String country)
	{
		setId(id);
		setUsername(username);
		setAge(age);
		setGender(gender);
		setCountry(country);
	}

	public User(Long id, String fullName, Integer age, Gender gender,
			String country, String email, String username)
	{
		setId(id);
		setFullName(fullName);
		setAge(age);
		setGender(gender);
		setCountry(country);
		setEmail(email);
		setUsername(username);
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public Integer getAge()
	{
		return age;
	}

	public void setAge(Integer age)
	{
		this.age = age;
	}

	public Gender getGender()
	{
		return gender;
	}

	public void setGender(Gender gender)
	{
		this.gender = gender;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = StringUtils.lowerCase(StringUtils.trim(email));
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = StringUtils.lowerCase(StringUtils.trim(username));
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
	
	public Long getTokenActiveTime()
	{
		return tokenActiveTime;
	}

	public void setTokenActiveTime(Long tokenActiveTime)
	{
		this.tokenActiveTime = tokenActiveTime;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "User 'fullName' can't be empty!")
	private String fullName;
	
	@NotNull(message = "User 'age' can't be empty!")
	@Min(value = 18, message = "User 'age' must be 18 or above!")
	private Integer age;
	
	@NotNull(message = "User 'gender' must be 'male' or 'female'!")
	private Gender gender;
	
	@NotBlank(message = "User 'country' can't be empty!")
	private String country;
	
	@Pattern(regexp = "[^@\\s]+@[^@\\s]+\\.[^@\\s]+",
			message = "User 'email' must follow a pattern similar to 'text@text.text'!")
	@NotBlank(message = "User 'email' can't be empty!")
	@Column(unique=true)	
	private String email;
	
	@Pattern(regexp = "[a-zA-Z0-9]{6,}",
			message = "User 'username' must be alphanumerics with at least 6 characters!")
	@NotBlank(message = "User 'username' can't be empty!")
	@Column(unique=true)
	private String username;
	
	@Pattern(regexp = "((?=.*[0-9])(?=.*[a-zA-Z])).{8,}",
			message = "User 'password' must have at least 1 digit, 1 letter and 8 characters!")
	@NotBlank(message = "User 'password' can't be empty!")
	private String password;
	
	@Column(unique=true)
	@NotBlank(message = "User 'token' can't be empty!")
	private String token;
	
	@NotNull(message = "User 'tokenActiveTime' can't be empty!")
	@Min(value = 0, message = "User 'tokenActiveTime' can't be less than 0!")
	private Long tokenActiveTime;
}
