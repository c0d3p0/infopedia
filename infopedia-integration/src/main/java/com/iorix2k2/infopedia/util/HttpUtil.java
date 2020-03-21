package com.iorix2k2.infopedia.util;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class HttpUtil
{
	public static HttpHeaders createDefaultHeaders()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return headers;
	}
	
	public static HttpHeaders getHeadersFromRequest(HttpServletRequest request)
	{
		Enumeration<String> headerNames = request.getHeaderNames();
		HttpHeaders headers = new HttpHeaders();
		if(headerNames != null)
		{
			while(headerNames.hasMoreElements())
			{
				String headerName = headerNames.nextElement();
				headers.add(headerName, request.getHeader(headerName));
			}
		}
		
		return headers;
	}
	
	public static String getHeaderValueAndValidate(HttpServletRequest request, String headerName)
	{
		String value = request.getHeader(headerName);
		
		if(StringUtils.isEmpty(value))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Inform '" + headerName + "' in the headers!");
		
		return value;
	}
	
	public static Map<String, String> getBasicAuthCredentials(HttpServletRequest request, String headerName)
	{
		String basicAuth = request.getHeader(headerName);
		Map<String, String> dataMap = new HashMap<>();
		
		if (basicAuth != null && basicAuth.toLowerCase().startsWith("basic"))
		{
		    String base64Credentials = basicAuth.trim().substring("Basic".length()).trim();
		    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		    String[] values = credentials.split(":", 2);
		    dataMap.put("username", values[0]);
		    
		    if(values.length > 1)
		    	dataMap.put("password", values[1]);
		}
		
		return dataMap;
	}
	
	public static Resource getBodyFromRequest(HttpServletRequest request)
	{
		try
		{
			return new InputStreamResource(request.getInputStream());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static String getURLSuffix(HttpServletRequest request)
	{
		ResourceUrlProvider urlProvider = (ResourceUrlProvider) request
				.getAttribute(ResourceUrlProvider.class.getCanonicalName());
		return "/" + urlProvider.getPathMatcher().extractPathWithinPattern(
				String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)),
				String.valueOf(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)));	    
	}
	
	public static String createDefaultRestErrorBody(ObjectMapper objectMapper,
			DateFormat dateFormat, HttpStatus httpStatus, String message, String path)
	{
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.put("timestamp", dateFormat.format(new Date()));
    objectNode.put("status", httpStatus.value());
    objectNode.put("error", httpStatus.getReasonPhrase());
    objectNode.put("message", message);
    objectNode.put("path", path);
    return objectNode.toPrettyString();
	}
}
