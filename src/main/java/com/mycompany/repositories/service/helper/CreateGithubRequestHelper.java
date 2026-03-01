package com.mycompany.repositories.service.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.mycompany.repositories.constant.Constant;
import com.mycompany.repositories.http.HttpRequest;
import com.mycompany.repositories.pojo.GithubRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreateGithubRequestHelper {
	
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
		
		
		String url = UriComponentsBuilder
                .fromUriString(githubBaseUrlPath)
                .queryParam(Constant.QUERY_PARAM_Q , createGithubRequest.getQuery() 
                		+ Constant.LANGUAGE + createGithubRequest.getLanguage())
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

}
