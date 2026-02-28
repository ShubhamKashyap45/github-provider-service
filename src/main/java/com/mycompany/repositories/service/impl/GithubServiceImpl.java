package com.mycompany.repositories.service.impl;

import org.springframework.stereotype.Service;

import com.mycompany.repositories.http.HttpRequest;
import com.mycompany.repositories.http.HttpServiceEngine;
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

	@Override
	public String searchRepo() {
		log.info("Inside searchRepo() method...");
		
		/*
		 * Preparing githubRequest in Helper function
		 */
		HttpRequest httpRequest = createGithubRequestHelper
				.prepareCreateGithubRequest();
		
		String httpResponse = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("Responsed recieved in httpResponse: {}", httpResponse);
		return httpResponse;
	}


}
