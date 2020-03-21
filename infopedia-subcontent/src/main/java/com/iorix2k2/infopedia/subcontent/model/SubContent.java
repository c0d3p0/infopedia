package com.iorix2k2.infopedia.subcontent.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubContent
{
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

	public Long getArticleId()
	{
		return articleId;
	}
	
	public void setArticleId(Long articleId)
	{
		this.articleId = articleId;
	}
	
	public Short getPosition()
	{
		return position;
	}
	
	public void setPosition(Short position)
	{
		this.position = position;
	}
	
	public SubContentType getType()
	{
		return type;
	}
	
	public void setType(SubContentType type)
	{
		this.type = type;
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
	
	@NotNull(message = "Sub content 'userId' can't be null!")
	private Long userId;
	
	@NotNull(message = "Sub content 'articleId' can't be null!")
	private Long articleId;
	
	@NotNull(message = "Sub content 'position' can't be empty!")
	@Min(value = 1, message = "Sub content 'position' can't be less than 1!")
	private Short position;
	
	@NotNull(message = "Sub content 'type' must be 'text', 'image-link' or 'link'!")
	private SubContentType type;
	
	@NotBlank(message = "Sub content 'title' can't be empty!")
	private String title;
	
	@NotBlank(message = "Sub content 'content' can't be empty!")
	@Column(columnDefinition="LONG VARCHAR")
	private String content;
}
	