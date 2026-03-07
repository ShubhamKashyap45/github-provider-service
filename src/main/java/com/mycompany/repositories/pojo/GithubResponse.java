package com.mycompany.repositories.pojo;

import java.util.List;

import lombok.Data;

@Data
public class GithubResponse {
	
	private String message;
	private List<Repository> repositories;

}
