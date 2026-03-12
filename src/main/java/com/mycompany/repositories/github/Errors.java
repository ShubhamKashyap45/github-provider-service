package com.mycompany.repositories.github;

import lombok.Data;

@Data
public class Errors {
	
	private String resource;
	private String field;
	private String code;

}
