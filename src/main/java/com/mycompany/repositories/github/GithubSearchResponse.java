package com.mycompany.repositories.github;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class GithubSearchResponse {
	
    @JsonProperty("total_count")
    private int totalCount;
    
    @JsonProperty("incomplete_results")
    private boolean incompleteResults;
    
    @JsonProperty("items")
    private List<GithubRepositoryApiItem> items;

}
