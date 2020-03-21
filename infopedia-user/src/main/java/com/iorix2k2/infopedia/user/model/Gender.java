package com.iorix2k2.infopedia.user.model;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum Gender
{
	MALE("male"),
	FEMALE("female");
	
	private Gender(String text)
	{
		this.text = text;
	}
	
	@JsonValue
	public String getText()
	{
		return text;
	}
	
	@JsonCreator
	public static Gender createFromText(String text)
	{
		String t = StringUtils.defaultString(text).toLowerCase().trim();
		
		if(t.equals("male"))
			return Gender.MALE;
		else if(t.equals("female"))
			return Gender.FEMALE;
		
		return null;
	}
	
	
	private String text;
}
