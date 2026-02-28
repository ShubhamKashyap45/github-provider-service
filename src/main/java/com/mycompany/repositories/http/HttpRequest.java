package com.mycompany.repositories.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import lombok.Data;

@Data
public class HttpRequest {
	
	private HttpMethod httpMethod;
	private String path;
	private String query = "spring";
	private String language = "java";
	private String sort = "stars";
	private HttpHeaders httpHeaders;

}
