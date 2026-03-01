package com.mycompany.repositories.http;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpServiceEngine {
	
	private final RestClient restClient;
	
	public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
		log.info("Inside makeHttpCall method, Making HTTP "
				+ "call to github external service....");
		
		
		ResponseEntity<String> httpResponse = restClient.method(
				httpRequest.getHttpMethod())
				.uri(httpRequest.getUrl())
				.headers(restClientHeaders -> restClientHeaders.addAll(
						httpRequest.getHttpHeaders()))
				.retrieve()
				.toEntity(String.class);
		
		  log.info("HTTP call completed. Response recieved from github API");
		
		return httpResponse;
	}
	
	@PostConstruct
	public void init() {
		log.info("Initlizing HttpServiceEngine: {}", restClient);
	}

}
