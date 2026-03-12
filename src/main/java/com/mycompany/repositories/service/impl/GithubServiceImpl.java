package com.mycompany.repositories.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.repositories.dto.GithubRepositoryDTO;
import com.mycompany.repositories.entity.GithubRepositoryEntity;
import com.mycompany.repositories.github.GithubSearchResponse;
import com.mycompany.repositories.http.HttpRequest;
import com.mycompany.repositories.http.HttpServiceEngine;
import com.mycompany.repositories.mapper.GithubMapper;
import com.mycompany.repositories.pojo.GithubRequest;
import com.mycompany.repositories.pojo.GithubResponse;
import com.mycompany.repositories.repository.GithubRepository;
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
	
	private final GithubMapper mapper;
	
	private final GithubRepository githubRepository;
	
	
	/**
	 * Searches GitHub repositories based on the provided GithubRequest
	 * and returns a mapped GithubResponse.
	 *
	 * Flow:
	 * 1. Logs and validates the incoming GithubRequest.
	 * 2. Prepares the GitHub API HTTP request using CreateGithubRequestHelper.
	 * 3. Executes the HTTP call using HttpServiceEngine.
	 * 4. Processes the GitHub API response into GithubSearchResponse.
	 * 5. Maps GithubSearchResponse to internal GithubRepositoryDTO objects.
	 * 6. Converts GithubRepositoryDTO to GithubRepositoryEntity objects.
	 * 7. Persists the repository entities into the PostgreSQL database using saveAll() 
	 *    (performs UPSERT based on repository ID as primary key).
	 * 8. Maps GithubSearchResponse to the final GithubResponse.
	 * 9. Returns the GithubResponse to the client.
	 */
	
	@Override
	public GithubResponse searchRepo(GithubRequest createGithubRequest) {
		log.info("Inside searchRepo method... "
				+ "createGithubRequest: {}", createGithubRequest);
		
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
		 * Process 2xx, 4xx/5xx response received from GitHub API
		 */
		GithubSearchResponse githubSearchResponse = createGithubRequestHelper
				.processGithubResponse(httpResponse);
		
		log.info("GitHub API response processed successfully");
		
		/*
		 * Map the GitHub API response to internal DTO object.
		 */
		List<GithubRepositoryDTO> githubRepositoryDTO = mapper.
				mapGithubSearchResponseToRepositoryDTO(githubSearchResponse);
		
		log.info("Mapped GitHub API response to githubSearchResponse: {}",
		        githubRepositoryDTO.size());
		
		/*
		 * Map DTO object to Entity object.
		 */
		List<GithubRepositoryEntity> githubRepositoryEntity = mapper
				.mapRepositoryDTOToEntity(githubRepositoryDTO);
		
		log.info("Converted githubRepositoryDTO object to "
				+ "githubRepositoryEntity: {}",
		        githubRepositoryEntity.size());
		
		/*
		 * Persist repository data into the PostgreSQL database.
		 * saveAll() performs batch persistence. Since the repository ID
		 * is marked as the primary key, Hibernate automatically performs
		 * an UPSERT operation:
		 *   - If the repository ID does not exist → INSERT
		 *   - If the repository ID already exists → UPDATE
		 */
		List<GithubRepositoryEntity> savedEntities = githubRepository
				.saveAll(githubRepositoryEntity);
		
		log.info("Saved {} repositories to database", savedEntities.size());
		
		
		/*
		 * Map the GitHub API response to the final response
		 * that will be returned to the client.
		 */
		
		GithubResponse githubResponse = mapper
				.mapGithubSearchResponseToGithubResponse(githubSearchResponse);
		
		log.info("Mapped githubSearchResponse to githubResponse: {}", githubResponse);
		
		return githubResponse;
	}


	@Override
	public GithubResponse getRepositories() {
		log.info("Inside getRepositories");
		
		/*
		 * Fetching data from the DB
		 */
		List<GithubRepositoryEntity> fetchedData = githubRepository.findAll();
		
		log.info("Repositores fetched from DB in fetchData: {}", fetchedData);
		
		
		/*
		 * Map Entity object to DTO object
		 */
		List<GithubRepositoryDTO> githubRepositoryDTO = mapper
				.mapEntityToDTO(fetchedData);
		
		log.info("Data in githubRepositoryDTO: {}", githubRepositoryDTO);
		
		/*
		 *  Map DTO to GithubResponse POJO
		 */
		GithubResponse githubResponse = mapper
				.mapRepositoryDTOToGithubResponse(githubRepositoryDTO);
		
		log.info("Mapped values in githubResponse: {}", githubResponse);
		
		return githubResponse;
		
	}

}
