package com.iorix2k2.infopedia.article.error;


import java.io.IOException;
import java.text.DateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iorix2k2.infopedia.util.HttpUtil;


@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint
{
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException
	{
		var url = request.getRequestURI();
		var message = "Invalid request!";
		var body = HttpUtil.createDefaultRestErrorBody(objectMapper,
        defaultDateFormat, HttpStatus.BAD_REQUEST, message, url);
		response.getWriter().append(body);
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	
			
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DateFormat defaultDateFormat;
}