package com.mycompany.repositories.service.impl;

import org.springframework.stereotype.Service;

import com.mycompany.repositories.http.HttpServiceEngine;
import com.mycompany.repositories.service.interfaces.GithubService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {
	
	public final HttpServiceEngine httpServiceEngine;

	@Override
	public String searchRepo() {
		log.info("Inside githubRepo() method...");
		
		String httpResponse = httpServiceEngine.makeHttpCall();
		log.info("Response recieved: {}", httpResponse);
		return httpResponse;
	}

}
