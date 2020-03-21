package com.iorix2k2.infopedia.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


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

	
	private Long id;
	private Long userId;
	private Long articleId;
	private Short position;
	private SubContentType type;
	private String title;
	private String content;
}
	