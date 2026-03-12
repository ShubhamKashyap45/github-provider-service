package com.mycompany.repositories.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GithubResponse {
	
	private String message;
	private List<Repository> repositories;

}
