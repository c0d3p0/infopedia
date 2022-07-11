package com.iorix2k2.infopedia.article.error;

import java.text.DateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iorix2k2.infopedia.util.HttpUtil;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(value = {InvalidDataException.class})
	protected ResponseEntity<Object> handleInvalidDataException(
			InvalidDataException ex, WebRequest request)
	{
		var httpStatus = getHttpStatus(ex);
		var httpHeaders = HttpUtil.createDefaultHeaders();
		var path = HttpUtil.getRequestPath(request);
		var body = HttpUtil.createDefaultRestErrorBody(
				objectMapper, defaultDateFormat, httpStatus, ex, path);
		return handleExceptionInternal(ex, body, httpHeaders, httpStatus, request);
	}

	@ExceptionHandler(value = {ResponseStatusException.class})
	protected ResponseEntity<Object> handleResponseStatusException(
			ResponseStatusException ex, WebRequest request)
	{
		var httpStatus = ex.getStatus();
		var httpHeaders = HttpUtil.createDefaultHeaders();
		var path = HttpUtil.getRequestPath(request);
		var body = HttpUtil.createDefaultRestErrorBody(objectMapper,
				defaultDateFormat, httpStatus, ex, ex.getReason(), path);
		return handleExceptionInternal(ex, body, httpHeaders, httpStatus, request);
	}

	@ExceptionHandler
	protected ResponseEntity<Object> handleDefaultException(Exception ex, WebRequest request)
	{
		var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		var httpHeaders = HttpUtil.createDefaultHeaders();
		var path = HttpUtil.getRequestPath(request);
		var body = HttpUtil.createDefaultRestErrorBody(
				objectMapper, defaultDateFormat, httpStatus, ex, path);
		return handleExceptionInternal(ex, body, httpHeaders, httpStatus, request);
	}

	private HttpStatus getHttpStatus(InvalidDataException ex)
	{
		var type = ex.getInvalidDataExceptionType();
		
		if(type == InvalidDataExceptionType.DUPLICATED_DATA)
			return HttpStatus.CONFLICT;

		if(type == InvalidDataExceptionType.UNMATCHED_EXPIRED_UNAUTHORIZED_DATA)
			return HttpStatus.UNAUTHORIZED;
		
		return HttpStatus.BAD_REQUEST;
	}
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DateFormat defaultDateFormat;
}