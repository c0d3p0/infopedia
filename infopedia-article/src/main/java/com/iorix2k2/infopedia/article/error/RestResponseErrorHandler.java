package com.iorix2k2.infopedia.article.error;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class RestResponseErrorHandler extends DefaultResponseErrorHandler
{
  @Override
  public void handleError(ClientHttpResponse response) throws IOException
  {
  	ObjectNode on = objectMapper.readValue(response.getBody(), ObjectNode.class);
  	throw new ResponseStatusException(response.getStatusCode(), on.get("message").asText());
  }
  
  
  @Autowired
  private ObjectMapper objectMapper;
}