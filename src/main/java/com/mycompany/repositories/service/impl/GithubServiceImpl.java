package com.mycompany.repositories.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.repositories.exception.GithubProviderException;
import com.mycompany.repositories.github.GithubSearchResponse;
import com.mycompany.repositories.http.HttpRequest;
import com.mycompany.repositories.http.HttpServiceEngine;
import com.mycompany.repositories.pojo.GithubRequest;
import com.mycompany.repositories.pojo.GithubResponse;
import com.mycompany.repositories.pojo.Repository;
import com.mycompany.repositories.service.helper.CreateGithubRequestHelper;
import com.mycompany.repositories.service.interfaces.GithubService;
import com.mycompany.repositories.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

	private final HttpServiceEngine httpServiceEngine;
	
	private final CreateGithubRequestHelper createGithubRequestHelper;
	
	private final JsonUtil jsonUtil;


	@Override
	public GithubResponse searchRepo(GithubRequest createGithubRequest) {
		log.info("Inside searchRepo method...");
		
		if(createGithubRequest.getQuery() == null || 
				createGithubRequest.getQuery().isEmpty()) {
			
			log.error("Validation failed: 'query' field missing in "
					+ "the request body");
			
			throw new GithubProviderException(
					"GHP03-400-01", 
					"Required filed 'query' missing in the request body", 
					HttpStatus.BAD_REQUEST);
		}
				
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
		 * Converting JSON String to GithubSearchResponse Class 
		 */
		GithubSearchResponse githubSearchResponse = jsonUtil.convertJsonToObject(
				httpResponse.getBody(), GithubSearchResponse.class);

		log.info("JSON String converted to Object "
				+ "githubSearchResponse: {}", githubSearchResponse);
		
		
		GithubResponse githubResponse = mapGithubSearchResponseToGithubResponse(
						githubSearchResponse);
		
		log.info("Responsed recieved from HttpServiceEngine....");
		return githubResponse;
	}


	private GithubResponse mapGithubSearchResponseToGithubResponse(
			GithubSearchResponse githubSearchResponse) {
		
		if(githubSearchResponse == null) {
			log.debug("mapGithubSearchResponseToGithubRepositoryResponse "
					+ "called with null");
			return null;
		}
		
		GithubResponse githubResponse = new GithubResponse();

	    // Map the list of GithubRepositoryApiItem to Repository
	    if (githubSearchResponse.getItems() != null) {
	        List<Repository> repositories = githubSearchResponse.getItems()
	        		.stream()
	        		.map(item -> {
	            Repository repo = new Repository();
	            repo.setId(item.getId());
	            repo.setName(item.getName());
	            repo.setDescription(item.getDescription());
	            repo.setOwner(item.getOwner().getLogin());
	            repo.setLanguage(item.getLanguage());
	            repo.setStars(item.getStars());
	            repo.setForks(item.getForks());
	            repo.setLastUpdated(item.getLastUpdated());
	            return repo;
	        }).toList();
	        
	        githubResponse.setRepositories(repositories);
	        githubResponse.setMessage("Repositories fetched successfully");
	    }

	    log.info("Mapped GithubSearchResponse to GithubResponse: {}", githubResponse);

	    return githubResponse;
		
	}


}
