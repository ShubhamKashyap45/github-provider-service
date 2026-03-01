package com.mycompany.repositories.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.repositories.pojo.GithubRequest;
import com.mycompany.repositories.pojo.GithubResponse;
import com.mycompany.repositories.service.interfaces.GithubService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/github")
public class GithubController {
	
	private final GithubService githubService;
	
	@PostMapping("/search")
	public GithubResponse searchRepositories(@RequestBody GithubRequest createGithubRequest) {
		log.info("Inside searchRepositores method "
				+ "Received GitHub search request in "
				+ "createGithubRequest: {}", createGithubRequest);
		
		GithubResponse response = githubService.searchRepo(createGithubRequest);
		
		log.info("Response reviced from GithubServiceImp, "
				+ "sending response to Client....");
		
		return response;
		
	}

}
