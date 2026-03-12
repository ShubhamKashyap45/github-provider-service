package com.mycompany.repositories.github;

import java.util.List;

import lombok.Data;

@Data
public class GithubErrorResponse {
	
	private String message;
	private List<Errors> errors;

}
