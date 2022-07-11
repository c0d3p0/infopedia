package com.iorix2k2.infopedia.integration.error;

import java.io.IOException;
import java.text.DateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iorix2k2.infopedia.util.HttpUtil;


@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler
{
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException
	{
		var url = request.getRequestURI().toString();
		var message = "You are not authorized to access this feature!";
		var body = HttpUtil.createDefaultRestErrorBody(
				objectMapper, defaultDateFormat, HttpStatus.UNAUTHORIZED, message, url);
		response.getWriter().append(body);
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}


	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DateFormat defaultDateFormat;
}