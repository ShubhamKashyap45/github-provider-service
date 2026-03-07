package com.mycompany.repositories.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mycompany.repositories.github.GithubSearchResponse;
import com.mycompany.repositories.pojo.GithubResponse;
import com.mycompany.repositories.pojo.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MapGithubSearchResponseToGithubResponse {
	
	public GithubResponse mapGithubSearchResponseToGithubResponse(
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
