package com.iorix2k2.infopedia.integration.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Article
{
	public Article()
	{
		subContentList = new ArrayList<>();
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
	
	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public List<SubContent> getSubContentList()
	{
		return subContentList;
	}

	public void setSubContentList(List<SubContent> subContentList)
	{
		this.subContentList = subContentList;
	}


	private Long id;
	private Long userId;
	private String title;
	private String content;
	private User user;
	private List<SubContent> subContentList;
}
