package com.mycompany.repositories.service.helper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.mycompany.repositories.constant.Constant;
import com.mycompany.repositories.constant.ErrorCodeEnum;
import com.mycompany.repositories.exception.GithubProviderException;
import com.mycompany.repositories.github.GithubErrorResponse;
import com.mycompany.repositories.github.GithubSearchResponse;
import com.mycompany.repositories.http.HttpRequest;
import com.mycompany.repositories.pojo.GithubRequest;
import com.mycompany.repositories.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateGithubRequestHelper {
	
	private final JsonUtil jsonUtil;
	
	@Value("${github.baseurl.path}")
	private String githubBaseUrlPath;

	public HttpRequest prepareCreateGithubRequest(
			GithubRequest createGithubRequest) {
		
		log.info("Inside prepareCreateGithubRequest method preparing httpRequest "
				+ "for Github GET API....."
				+ "createGithubRequest: {}", createGithubRequest);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
		httpHeaders.set(Constant.GITHUB_API_VERSION_HEADER, 
				Constant.GITHUB_API_VERSION);
		
	    String query = createGithubRequest.getQuery()
                + Constant.SEARCH_IN_NAME
                + Constant.LANGUAGE
                + createGithubRequest.getLanguage();
		
		
		String url = UriComponentsBuilder
                .fromUriString(githubBaseUrlPath)
                .queryParam(Constant.QUERY_PARAM_Q , query)
                .queryParam(Constant.QUERY_PARAM_SORT, createGithubRequest.getSort())
                .queryParam(Constant.QUERY_PARAM_ORDER, 
                		Constant.GITHUB_SEARCH_ORDER_DESCENDING)
                .build()
                .toUriString();
		
		
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpMethod(HttpMethod.GET);
        httpRequest.setUrl(url);
        httpRequest.setHttpHeaders(httpHeaders);
		
		log.info("Values set in httpRequest: {}", httpRequest);
		return httpRequest;
	}
	
	public GithubSearchResponse processGithubResponse(
			ResponseEntity<String> httpResponse) {
		
		/*
		 * Handle 2xx success case
		 */
		if(httpResponse.getStatusCode().is2xxSuccessful()) {
			log.info("Github Call Successfull. Status Code: {}, Response body: {}", 
					httpResponse.getStatusCode(), httpResponse.getBody());
			
			/*
			 * Converting JSON String 2xx response to GithubSearchResponse Class 
			 */
			GithubSearchResponse githubSearchResponse = jsonUtil
					.convertJsonToObject(httpResponse.getBody(), 
							GithubSearchResponse.class);
			
			if(githubSearchResponse != null 
					&& githubSearchResponse.getTotalCount() > 0 
					&& githubSearchResponse.getItems() != null
					&& !githubSearchResponse.getItems().isEmpty()) {
				
				return githubSearchResponse;
			}
			
			log.error("API call returned 2xx but response body is empty or invalid. "
					+ "Status code: {}, Response body: {}", 
			httpResponse.getStatusCode(), httpResponse.getBody());
			
			throw new GithubProviderException(
					ErrorCodeEnum.EMPTY_SEARCH_RESULTS.getErrorCode(), 
					ErrorCodeEnum.EMPTY_SEARCH_RESULTS.getErrorMessage(), 
					HttpStatus.NOT_FOUND);
		}
		
		/*
		 * Handle 4xx/5xx valid failed errors
		 */
		if(httpResponse.getStatusCode() == HttpStatus.FORBIDDEN || 
				httpResponse.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
			log.error("Github API rate limit exceeded");
			
			/*
			 * Converting JSON String Error response to GithubSearchResponse Class 
			 */
			GithubErrorResponse githubError = jsonUtil
					.convertJsonToObject(httpResponse.getBody(), 
							GithubErrorResponse.class);
			
			if(githubError != null && githubError.getMessage() != null && 
					githubError.getMessage()
					.toLowerCase().contains("rate limit exceeded")) {
				
				log.error("GitHub API rate limit exceeded: {}", 
						githubError.getMessage());
				
		        throw new GithubProviderException(
		        		ErrorCodeEnum.GITHUB_RATE_LIMIT_EXCEEDED.getErrorCode(),
		                ErrorCodeEnum.GITHUB_RATE_LIMIT_EXCEEDED.getErrorMessage(),
		                HttpStatus.TOO_MANY_REQUESTS);
			}
		}
		
		if(httpResponse.getStatusCode().is4xxClientError() 
				|| httpResponse.getStatusCode().is5xxServerError()) {
			
			log.error("Github API call failed. Staus Code: {}, Response body: {}", 
					httpResponse.getStatusCode(), httpResponse.getBody());
			
			/*
			 * Converting JSON String Error response to GithubSearchResponse Class 
			 */
			GithubErrorResponse githubError = jsonUtil.convertJsonToObject(
					httpResponse.getBody(), GithubErrorResponse.class);
			
			if(githubError != null && githubError.getMessage() != null) {
				
				log.error("Github API error message: {}", githubError.getMessage());
				
				githubError.getErrors().forEach(error -> 
					log.error("Resource: {}, Field: {}, Code: {}", 
							error.getResource(), 
							error.getField(), 
							error.getCode()));
				
				String githubConcatinatedErrorMessage = 
						prepareGithubErrorMessage(githubError);
				
				log.error("Prepare Github Error Message: {}", 
						githubConcatinatedErrorMessage);
				
				throw new GithubProviderException(
						ErrorCodeEnum.GITHUB_API_ERROR.getErrorCode(), 
						githubConcatinatedErrorMessage, 
						HttpStatus.valueOf(httpResponse.getStatusCode().value()));
			}
			
			log.error("Github API call failed with non-JSON error response."
					+ "Status code: {}, Response body: {}", 
					httpResponse.getStatusCode(), httpResponse.getBody());
		}
		
		/*
		 * Handle success object conversion failed
		 * Unable to parse error response body to GithubErrorResponse object
		 */
		throw new GithubProviderException(
				ErrorCodeEnum.INVALID_GITHUB_RESPNOSE.getErrorCode(), 
				ErrorCodeEnum.INVALID_GITHUB_RESPNOSE.getErrorMessage(), 
				HttpStatus.BAD_GATEWAY);
	}


	private String prepareGithubErrorMessage(GithubErrorResponse 
			githubError) {
		
	    String baseMessage = githubError.getMessage();

	    if (githubError.getErrors() == null || githubError.getErrors().isEmpty()) {
	        return baseMessage;
	    }

	    String errorDetails = githubError.getErrors().stream()
	            .map(error -> Stream.of(
	                        error.getResource(),
	                        error.getField(),
	                        error.getCode())
	                    .filter(Objects::nonNull)
	                    .collect(Collectors.joining(":")))
	            		.collect(Collectors.joining(" | "));

	    return baseMessage + " | " + errorDetails;
	}

}
