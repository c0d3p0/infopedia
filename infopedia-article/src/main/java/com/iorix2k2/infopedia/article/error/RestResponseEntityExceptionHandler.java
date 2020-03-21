package com.iorix2k2.infopedia.article.error;

import java.text.DateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iorix2k2.infopedia.util.HttpUtil;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(value = {InvalidDataException.class})
	protected ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex,
			WebRequest request)
	{  	
		HttpStatus httpStatus = getHttpStatus(ex);
		HttpHeaders httpHeaders = HttpUtil.createDefaultHeaders();
		String path = request.getAttribute(HandlerMapping.LOOKUP_PATH, 0).toString();
		String body = HttpUtil.createDefaultRestErrorBody(objectMapper,
				defaultDateFormat, httpStatus, ex.getMessage(), path);
		return handleExceptionInternal(ex, body, httpHeaders, httpStatus, request);
	}

	private HttpStatus getHttpStatus(InvalidDataException ex)
	{
		InvalidDataExceptionType type = ex.getInvalidDataExceptionType();
		
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