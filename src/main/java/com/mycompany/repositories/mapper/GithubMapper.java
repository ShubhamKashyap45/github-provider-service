package com.mycompany.repositories.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mycompany.repositories.dto.GithubRepositoryDTO;
import com.mycompany.repositories.entity.GithubRepositoryEntity;
import com.mycompany.repositories.github.GithubSearchResponse;
import com.mycompany.repositories.pojo.GithubResponse;
import com.mycompany.repositories.pojo.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GithubMapper {
	
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
	        githubResponse.setMessage("Repositories fetched and saved successfully");
	    }

	    return githubResponse;
		
	}
	
    public List<GithubRepositoryDTO> mapGithubSearchResponseToRepositoryDTO(
            GithubSearchResponse githubSearchResponse) {

        if (githubSearchResponse == null) {
            log.debug("mapGithubSearchResponseToRepositoryDTO called with null");
            return null;
        }

        if (githubSearchResponse.getItems() == null) {
            return List.of();
        }

        List<GithubRepositoryDTO> repositories = githubSearchResponse.getItems()
                .stream()
                .map(item -> {

                    GithubRepositoryDTO dto = new GithubRepositoryDTO();

                    dto.setId(item.getId());
                    dto.setName(item.getName());
                    dto.setDescription(item.getDescription());
                    dto.setOwner(item.getOwner().getLogin());
                    dto.setLanguage(item.getLanguage());
                    dto.setStars(item.getStars());
                    dto.setForks(item.getForks());

                    LocalDateTime lastUpdated =
                            OffsetDateTime.parse(item.getLastUpdated())
                            .toLocalDateTime();

                    dto.setLastUpdated(lastUpdated);

                    return dto;

                })
                .toList();

        log.info("Mapped GithubSearchResponse to GithubRepositoryDTO list");

        return repositories;
    }
    
    
    public List<GithubRepositoryEntity> mapRepositoryDTOToEntity(
            List<GithubRepositoryDTO> repositoryDTOList) {

        if (repositoryDTOList == null) {
            log.debug("mapRepositoryDTOToEntity called with null");
            return null;
        }

        List<GithubRepositoryEntity> entities = repositoryDTOList
                .stream()
                .map(dto -> {

                    GithubRepositoryEntity entity = new GithubRepositoryEntity();

                    entity.setId(dto.getId());
                    entity.setName(dto.getName());
                    entity.setDescription(dto.getDescription());
                    entity.setOwner(dto.getOwner());
                    entity.setLanguage(dto.getLanguage());
                    entity.setStars(dto.getStars());
                    entity.setForks(dto.getForks());
                    entity.setLastUpdated(dto.getLastUpdated());

                    return entity;

                })
                .toList();

        log.info("Mapped GithubRepositoryDTO list to GithubRepositoryEntity list");

        return entities;
    }
}

