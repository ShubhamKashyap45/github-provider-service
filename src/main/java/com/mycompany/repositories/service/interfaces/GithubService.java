package com.mycompany.repositories.service.interfaces;

import com.mycompany.repositories.pojo.GithubRequest;
import com.mycompany.repositories.pojo.GithubResponse;

public interface GithubService {
	
	public GithubResponse searchRepo(GithubRequest createGithubRequest);

	public GithubResponse getRepositories();

}
