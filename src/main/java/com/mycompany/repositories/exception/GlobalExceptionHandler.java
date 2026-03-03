package com.mycompany.repositories.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mycompany.repositories.pojo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(GithubProviderException.class)
	public ResponseEntity<ErrorResponse> handleGithubException(GithubProviderException ex) {
		
		log.error("GithubProviderException caught: {}", ex.toString());
		
		HttpStatus status = ex.getHttpStatus() != null ? 
				ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
		
		ErrorResponse body = new ErrorResponse();
		body.setErrorCode(ex.getErrorCode());
		body.setErrorMessage(ex.getErrorMessage());
		return new ResponseEntity<>(body, status);
		
	}
	
	

}
