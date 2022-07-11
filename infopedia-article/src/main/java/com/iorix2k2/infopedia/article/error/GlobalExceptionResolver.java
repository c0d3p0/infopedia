package com.iorix2k2.infopedia.article.error;


import java.text.DateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iorix2k2.infopedia.util.HttpUtil;


public class GlobalExceptionResolver implements HandlerExceptionResolver
{
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
	{
		var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		var path = request.getRequestURI();
		var body = HttpUtil.createDefaultRestErrorBody(
				objectMapper, defaultDateFormat, httpStatus, ex, path);
		response.setHeader("Content-Type", "application/json");
		response.setStatus(httpStatus.value());

		try
		{
			response.getWriter().write(body);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return new ModelAndView();
	}


	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DateFormat defaultDateFormat;
}