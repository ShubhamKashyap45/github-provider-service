package com.mycompany.repositories.http;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
	
	public String makeHttpCall() {
		log.info("Inside makeHttpCall method, Making HTTP "
				+ "call to external service....");
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
		httpHeaders.set("X-GitHub-Api-Version", "2022-11-28");
		
		ResponseEntity<String> httpRespone = restClient.method(HttpMethod.GET)
		.uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host("api.github.com")
                .path("/search/repositories")
                .queryParam("q", "spring language:java") 
                .queryParam("sort", "stars")
                .queryParam("order", "desc")
                .build())
        .headers(restClientHeaders -> restClientHeaders.addAll(httpHeaders))
        .retrieve()
        .toEntity(String.class);
        
        log.info("HTTP call completed. "
        		+ "\n Status Code: {}, "
        		+ "\n Response body: {}", httpRespone.getStatusCode(), 
        		httpRespone.getBody());
		
		return httpRespone.getBody();
	}
	
	@PostConstruct
	public void init() {
		log.info("Initlizing HttpServiceEngine: {}", restClient);
	}

}
