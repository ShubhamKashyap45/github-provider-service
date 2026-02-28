package com.mycompany.repositories.service.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.mycompany.repositories.constant.Constant;
import com.mycompany.repositories.http.HttpRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreateGithubRequestHelper {
	
	@Value("${github.baseurl.path}")
	private String githubBaseUrlPath;

	public HttpRequest prepareCreateGithubRequest() {
		
		log.info("Inside prepareCreateGithubRequest() method.......");
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
		httpHeaders.set(Constant.GITHUB_API_VERSION_HEADER, Constant.GITHUB_API_VERSION);
		
		
		HttpRequest httpRequest = new HttpRequest();
		
		String qvalue = httpRequest.getQuery() + 
				Constant.LANGUAGE + httpRequest.getLanguage();
		
		httpRequest.setHttpMethod(HttpMethod.GET);
		httpRequest.setPath(githubBaseUrlPath);
		httpRequest.setQuery(qvalue);
		httpRequest.setHttpHeaders(httpHeaders);
		
		log.info("Values set in httpRequest: {}", httpRequest);
		return httpRequest;
	}

}
