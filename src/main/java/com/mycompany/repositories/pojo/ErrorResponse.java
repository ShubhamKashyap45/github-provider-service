package com.mycompany.repositories.pojo;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private String errorCode;
	private String errorMessage;

}
