package com.iorix2k2.infopedia.error;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;



public class RestResponseErrorHandler extends DefaultResponseErrorHandler
{	
	@Override
	public void handleError(ClientHttpResponse response) throws IOException
	{
		var on = objectMapper.readValue(response.getBody(), ObjectNode.class);
		var statusCode = response.getStatusCode();
		var message = on.get("message").asText();
		throw new ResponseStatusException(statusCode, message);
	}

	private ObjectMapper objectMapper = new ObjectMapper();
}