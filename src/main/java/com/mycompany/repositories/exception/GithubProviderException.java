package com.mycompany.repositories.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class GithubProviderException extends RuntimeException{
	
	
	private static final long serialVersionUID = 1L;
	
	private final String errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;
	
	public GithubProviderException(
			String errorCode, String errorMessage, HttpStatus httpStatus) {
		
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
	
	
	
	
	
	

}
