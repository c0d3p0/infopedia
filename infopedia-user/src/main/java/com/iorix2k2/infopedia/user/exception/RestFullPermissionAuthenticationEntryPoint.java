package com.iorix2k2.infopedia.user.exception;

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
public class RestFullPermissionAuthenticationEntryPoint implements AuthenticationEntryPoint
{
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException
	{
		String url = request.getAttribute("javax.servlet.forward.request_uri").toString();
		String message = "Invalid username/email or password/token!";
  	String body = HttpUtil.createDefaultRestErrorBody(objectMapper, defaultDateFormat,
  			HttpStatus.UNAUTHORIZED, message, url);
  	response.getWriter().append(body);
  	response.setContentType("application/json");
  	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
	
			
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private DateFormat defaultDateFormat;
}