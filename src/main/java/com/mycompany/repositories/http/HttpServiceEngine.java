package com.mycompany.repositories.http;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.mycompany.repositories.constant.Constant;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpServiceEngine {
	
	private final RestClient restClient;
	
	public String makeHttpCall(HttpRequest httpRequest) {
		log.info("Inside makeHttpCall method, Making HTTP "
				+ "call to external service....");
		
		
		ResponseEntity<String> httpResponse = restClient.method(
				httpRequest.getHttpMethod())
				.uri(uriBuilder -> uriBuilder
						.path(httpRequest.getPath())
						.queryParam(Constant.QUERY_PARAM_Q, httpRequest.getQuery()) 
						.queryParam(Constant.QUERY_PARAM_SORT, httpRequest.getSort())
						.queryParam(Constant.QUERY_PARAM_ORDER, 
								Constant.GITHUB_SEARCH_ORDER_DESCENDING)
						.build())
				.headers(restClientHeaders -> restClientHeaders.addAll(
						httpRequest.getHttpHeaders()))
				.retrieve()
				.toEntity(String.class);
        
        log.info("HTTP call completed. " + "\n Status Code: {}, "
        		+ "\n Response body: {}", httpResponse.getStatusCode(), 
        		httpResponse.getBody());
		
		return httpResponse.getBody();
	}
	
	@PostConstruct
	public void init() {
		log.info("Initlizing HttpServiceEngine: {}", restClient);
	}

}
