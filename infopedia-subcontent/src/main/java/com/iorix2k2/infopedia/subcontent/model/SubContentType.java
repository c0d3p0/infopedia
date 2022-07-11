package com.iorix2k2.infopedia.subcontent.model;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum SubContentType
{
	TEXT("text"),
	IMAGE_LINK("image-link"),
	LINK("link");
	
	
	private SubContentType(String text)
	{
		this.text = text;
	}
	
	@JsonValue
	public String getText()
	{
		return text;
	}

	@JsonCreator
	public static SubContentType createFromText(String text)
	{
		var t = StringUtils.defaultString(text).toLowerCase().trim();
		
		if(t.equals("text"))
			return SubContentType.TEXT;
		else if(t.equals("image-link"))
			return SubContentType.IMAGE_LINK;
		else if(t.equals("link"))
			return SubContentType.LINK;
		
		return null;
	}
	
	
	private String text;
}
