package com.iorix2k2.infopedia.util;


import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.util.ServletRequestPathUtils;
import org.springframework.web.util.UrlPathHelper;

import com.fasterxml.jackson.databind.ObjectMapper;


public class HttpUtil
{
	public static HttpHeaders createDefaultHeaders()
	{
		var headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return headers;
	}
	
	public static HttpHeaders getHeadersFromRequest(HttpServletRequest request)
	{
		var headerNames = request.getHeaderNames();
		var headers = new HttpHeaders();

		if(headerNames != null)
		{
			while(headerNames.hasMoreElements())
			{
				var headerName = headerNames.nextElement();
				headers.add(headerName, request.getHeader(headerName));
			}
		}
		
		return headers;
	}
	
	public static String getHeaderValueAndValidate(
			HttpServletRequest request, String headerName)
	{
		var value = request.getHeader(headerName);
		
		if(StringUtils.isEmpty(value))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Inform '" + headerName + "' in the headers!");
		}
		
		return value;
	}
	
	public static Map<String, String> getBasicAuthCredentials(
			HttpServletRequest request, String headerName)
	{
		var basicAuth = request.getHeader(headerName);
		var dataMap = new HashMap<String, String>();
		
		if (basicAuth != null && basicAuth.toLowerCase().startsWith("basic"))
		{
			var base64Credentials = basicAuth.trim().substring("Basic".length()).trim();
			var credDecoded = Base64.getDecoder().decode(base64Credentials);
			var credentials = new String(credDecoded, StandardCharsets.UTF_8);
			var values = credentials.split(":", 2);
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
		var urlProvider = (ResourceUrlProvider) request.
				getAttribute(ResourceUrlProvider.class.getCanonicalName());
		var suffix = "/" + urlProvider.getPathMatcher().extractPathWithinPattern(
				String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)),
				String.valueOf(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)));
		return suffix.endsWith("/") ? suffix.substring(0, suffix.length() - 1) : suffix;
	}
	
	public static String createDefaultRestErrorBody(ObjectMapper objectMapper,
			DateFormat dateFormat, HttpStatus httpStatus, Throwable t, String path)
	{
		var objectNode = objectMapper.createObjectNode();
		objectNode.put("timestamp", dateFormat.format(new Date()));
		objectNode.put("status", httpStatus.value());
		objectNode.put("error", httpStatus.getReasonPhrase());
		objectNode.put("message", t.getMessage());
		objectNode.put("path", path);
		objectNode.put("trace", ExceptionUtils.getStackTrace(t));
		return objectNode.toPrettyString();
	}

	public static String createDefaultRestErrorBody(
			ObjectMapper objectMapper, DateFormat dateFormat,
			HttpStatus httpStatus, Throwable t, String message, String path)
	{
		var m = message != null ? message : t.getMessage();
		var objectNode = objectMapper.createObjectNode();
		objectNode.put("timestamp", dateFormat.format(new Date()));
		objectNode.put("status", httpStatus.value());
		objectNode.put("error", httpStatus.getReasonPhrase());
		objectNode.put("message", m);
		objectNode.put("path", path);
		objectNode.put("trace", ExceptionUtils.getStackTrace(t));
		return objectNode.toPrettyString();
	}

	public static String createDefaultRestErrorBody(ObjectMapper objectMapper,
			DateFormat dateFormat, HttpStatus httpStatus, String message, String path)
	{
		var objectNode = objectMapper.createObjectNode();
		objectNode.put("timestamp", dateFormat.format(new Date()));
		objectNode.put("status", httpStatus.value());
		objectNode.put("error", httpStatus.getReasonPhrase());
		objectNode.put("message", message);
		objectNode.put("path", path);
		return objectNode.toPrettyString();
	}

	public static String getRequestPath(WebRequest request)
	{
		var path = (String) request.getAttribute(UrlPathHelper.PATH_ATTRIBUTE, 0);

		if(path == null)
		{
			var name = ServletRequestPathUtils.class.getName() + ".PATH";
			var attr = request.getAttribute(name, 0);
			path = attr.toString();
		}

		return path;
	}
}