package com.mycompany.repositories.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonUtil {
	
	private final ObjectMapper objectMapper;
	
	public <T> T convertJsonToObject(String json, Class<T> clazz) {
		if(json == null) {
			log.debug("convertJsonToObject called with null");
			return null;
		}
		
		if(clazz == null) {
			log.debug("convertJsonToObject with null clazz");
			return null;
		}
		
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("Failed to convert JSON to {}: {}", 
					clazz.getSimpleName(), e.getMessage());
			return null;
		}
	}
	
	
	public String convertObjectToJson(Object obj) {
		if(obj == null) {
			log.debug("convertObjectToJson called with null obj");
			return null;
		}
		
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("Failed to convert object to JSON: {}", e.getMessage());
			return null;
		}
	}

}
