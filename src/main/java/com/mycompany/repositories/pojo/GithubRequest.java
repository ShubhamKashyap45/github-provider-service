package com.mycompany.repositories.pojo;

import lombok.Data;

@Data
public class GithubRequest {
	
	private String query;
	private String language;
	private String sort;

}
