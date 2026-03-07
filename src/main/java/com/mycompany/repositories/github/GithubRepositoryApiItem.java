package com.mycompany.repositories.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class GithubRepositoryApiItem {
	
	private long id;
    private String name;
    private Owner owner;
    private String description;
    private String language;
    
    @JsonProperty("stargazers_count")
    private int stars;

    @JsonProperty("forks_count")
    private int forks;

    @JsonProperty("updated_at")
    private String lastUpdated;


}
