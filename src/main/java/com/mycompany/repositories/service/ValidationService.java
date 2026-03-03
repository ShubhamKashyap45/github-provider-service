package com.mycompany.repositories.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mycompany.repositories.exception.GithubProviderException;
import com.mycompany.repositories.pojo.GithubRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValidationService {

	public void isValid(GithubRequest req) {

		if (req == null) {
			log.error("Validation failed: request body missing");
			
			throw new GithubProviderException(
					"GHP03-400-01",
					"Request body is missing",
					HttpStatus.BAD_REQUEST);
		}
		
		if(req.getQuery() == null || req.getQuery().trim().isEmpty()) {
			log.error("Validation failed: 'query' field missing in "
					+ "the request body");

			throw new GithubProviderException(
					"GHP03-400-02", 
					"Required filed 'query' missing in the request body", 
					HttpStatus.BAD_REQUEST);
		}
		
		if(req.getLanguage() == null || req.getLanguage().trim().isEmpty()) {
			log.error("Validatoin failed: 'language' field missing "
					+ "in request body");
			
			throw new GithubProviderException(
					"GHP03-400-03", 
					"Required filed 'language' missing in the request body", 
					HttpStatus.BAD_REQUEST);
			
		}
		
		
	    if (req.getSort() != null &&
	            !List.of("stars", "forks", "updated").contains(req.getSort())) {
	    	
	    	log.error("Validation failed: Invalid sort parameter "
	    			+ "'"+ req.getSort() + "'");
	    	
	        throw new GithubProviderException(
	                "GHP03-400-04",
	                "Invalid sort parameter; must be 'stars', 'forks', or 'updated'",
	                HttpStatus.BAD_REQUEST
	        );
	    }
	    
	}


}

