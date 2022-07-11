package com.iorix2k2.infopedia.article.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article
{
	public Article() {}
	
	public Article(Long id, Long userId, String title, String Content)
	{
		setId(id);
		setUserId(userId);
		setTitle(title);
		setContent(Content);
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Article 'userId' can't be null!")
	private Long userId;
	
	@NotBlank(message = "Article 'title' can't be empty!")
	private String title;
	
	@Column(columnDefinition="CLOB")
	@NotBlank(message = "Article 'content' can't be empty!")
	private String content;
}
