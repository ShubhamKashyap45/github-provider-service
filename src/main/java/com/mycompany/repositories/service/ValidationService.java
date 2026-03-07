package com.mycompany.repositories.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mycompany.repositories.constant.ErrorCodeEnum;
import com.mycompany.repositories.constant.GithubLanguage;
import com.mycompany.repositories.constant.GithubSort;
import com.mycompany.repositories.exception.GithubProviderException;
import com.mycompany.repositories.pojo.GithubRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValidationService {
	
	private static final List<String> VALID_GITHUB_LANGUAGES = List.of(
			GithubLanguage.JAVA.getValue(),
            GithubLanguage.PYTHON.getValue(),
            GithubLanguage.JAVASCRIPT.getValue(),
            GithubLanguage.GO.getValue(),
            GithubLanguage.RUBY.getValue(),
            GithubLanguage.C_SHARP.getValue(),
            GithubLanguage.C_PLUS_PLUS.getValue(),
            GithubLanguage.PHP.getValue(),
            GithubLanguage.TYPESCRIPT.getValue(),
            GithubLanguage.KOTLIN.getValue()
	);

	public void isValid(GithubRequest req) {

		if (req == null) {
			log.error("Validation failed: request body missing");
			
			throw new GithubProviderException(
					ErrorCodeEnum.REQUEST_BODY_MISSING.getErrorCode(),
					ErrorCodeEnum.REQUEST_BODY_MISSING.getErrorMessage(),
					HttpStatus.BAD_REQUEST);
		}
		
		if(req.getQuery() == null || req.getQuery().trim().isEmpty()) {
			log.error("Validation failed: 'query' field missing in "
					+ "the request body");

			throw new GithubProviderException(
					ErrorCodeEnum.QUERY_MISSING.getErrorCode(), 
					ErrorCodeEnum.QUERY_MISSING.getErrorMessage(), 
					HttpStatus.BAD_REQUEST);
		}
		
		if(req.getLanguage() == null || req.getLanguage().trim().isEmpty()) {
			log.error("Validatoin failed: 'language' field missing "
					+ "in request body");
			
			throw new GithubProviderException(
					ErrorCodeEnum.LANGUAGE_MISSING.getErrorCode(), 
					ErrorCodeEnum.LANGUAGE_MISSING.getErrorMessage(), 
					HttpStatus.BAD_REQUEST);
			
		}
		
		if(!VALID_GITHUB_LANGUAGES.contains(req.getLanguage().toLowerCase())) {
			 log.error("Validation failed: Invalid GitHub language '{}'", 
					 req.getLanguage());
			 
			 throw new GithubProviderException(
		                ErrorCodeEnum.INVALID_GITHUB_LANGUAGE.getErrorCode(),
		                ErrorCodeEnum.INVALID_GITHUB_LANGUAGE.getErrorMessage(),
		                HttpStatus.BAD_REQUEST
		        );
		}
		
		
	    if (req.getSort() != null &&
	            !List.of(GithubSort.STARS.getValue(), GithubSort.FORKS.getValue(), 
	            		GithubSort.UPDATED.getValue()).contains(req.getSort())) {
	    	
	    	log.error("Validation failed: Invalid sort parameter "
	    			+ "'"+ req.getSort() + "'");
	    	
	        throw new GithubProviderException(
	                ErrorCodeEnum.INVALID_SORT.getErrorCode(),
	                ErrorCodeEnum.INVALID_SORT.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
	    }
	    
	}


}

