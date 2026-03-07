package com.mycompany.repositories.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.mycompany.repositories.constant.ErrorCodeEnum;
import com.mycompany.repositories.exception.GithubProviderException;

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
		
		try {
			ResponseEntity<String> httpResponse = restClient.method(
					httpRequest.getHttpMethod())
					.uri(httpRequest.getUrl())
					.headers(restClientHeaders -> restClientHeaders.addAll(
							httpRequest.getHttpHeaders()))
					.retrieve()
					.toEntity(String.class);
			
			  log.info("HTTP call completed. Response recieved from github API");
			
			return httpResponse;
			
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			
			log.error("Http error occured while making HTTP call: "
					+ "Status Code: {}, Response body: {}", 
					ex.getStatusCode(), ex.getResponseBodyAsString());
			
			if(ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE || 
					ex.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
				
				log.error("Github Service is unavailable. "
						+ "Status code: {}, Response body: {}", 
						ex.getStatusCode(), ex.getResponseBodyAsString());
				
				throw new GithubProviderException(
						ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE
						.getErrorCode(), 
						ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE
						.getErrorMessage(), 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			ResponseEntity<String> errorResponse = ResponseEntity
					.status(ex.getStatusCode())
					.body(ex.getResponseBodyAsString());
			
			return errorResponse;
			
		} catch (Exception e) {
			
			log.error("Error occured while connecting to Github API");
			
			throw new GithubProviderException(
					ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorCode(), 
					ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostConstruct
	public void init() {
		log.info("Initlizing HttpServiceEngine: {}", restClient);
	}

}
