package com.mycompany.repositories.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public String searchRepositories() {
		log.info("Inside searchRepositores method.....");
		
		String response = githubService.searchRepo();
		
		log.info("Response reviced: {}", response);
		
		return "Github Repositories fetched: " + response;
		
	}

}
