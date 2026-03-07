package com.mycompany.repositories.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.repositories.github.GithubSearchResponse;
import com.mycompany.repositories.http.HttpRequest;
import com.mycompany.repositories.http.HttpServiceEngine;
import com.mycompany.repositories.mapper.MapGithubSearchResponseToGithubResponse;
import com.mycompany.repositories.pojo.GithubRequest;
import com.mycompany.repositories.pojo.GithubResponse;
import com.mycompany.repositories.service.ValidationService;
import com.mycompany.repositories.service.helper.CreateGithubRequestHelper;
import com.mycompany.repositories.service.interfaces.GithubService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

	private final HttpServiceEngine httpServiceEngine;
	
	private final CreateGithubRequestHelper createGithubRequestHelper;
	
	private final ValidationService validationService;
	
	private final MapGithubSearchResponseToGithubResponse mapper;


	@Override
	public GithubResponse searchRepo(GithubRequest createGithubRequest) {
		log.info("Inside searchRepo method...");
		
		validationService.isValid(createGithubRequest);
				
		/*
		 * Preparing httpRequest in Helper function
		 */
		HttpRequest httpRequest = createGithubRequestHelper
				.prepareCreateGithubRequest(createGithubRequest);
		
		log.info("httpRequest prepared, passing data to makeHttpCall....");
		
		/*
		 *  Passing httpRequest to HttpServiceEngine, which is 
		 *  received from Helper function
		 */
		ResponseEntity<String> httpResponse = httpServiceEngine
				.makeHttpCall(httpRequest);
		
		/*
		 * Process 2xx, 4xx/5xx response received from github API
		 */
		GithubSearchResponse githubSearchResponse = createGithubRequestHelper
				.processGithubResponse(httpResponse);
		
		GithubResponse githubResponse = mapper.mapGithubSearchResponseToGithubResponse(
						githubSearchResponse);
		
		return githubResponse;
	}

}
