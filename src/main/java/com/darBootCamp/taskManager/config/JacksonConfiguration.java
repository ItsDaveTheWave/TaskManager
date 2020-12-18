package com.darBootCamp.taskManager.config;

import javax.annotation.PostConstruct;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfiguration {

	private final ObjectMapper objectMapper;
	
	public JacksonConfiguration(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@PostConstruct
	ObjectMapper jacksonObjectMapper() {
		objectMapper.registerModule(new JsonNullableModule());
		return objectMapper;
	}
}
