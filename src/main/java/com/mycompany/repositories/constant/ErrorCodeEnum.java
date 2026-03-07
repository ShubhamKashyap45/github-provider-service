package com.mycompany.repositories.constant;

public enum ErrorCodeEnum {
	
	GENERIC_ERROR("GHP03-500-000", 
			"An unexpected error occurred. Please try again later"),
	
    // Request-level errors
    REQUEST_BODY_MISSING("GHP03-400-01", "Request body is missing"),
    
    QUERY_MISSING("GHP03-400-02", 
    		"Required field 'query' missing in the request body"),
    
    LANGUAGE_MISSING("GHP03-400-03", 
    		"Required field 'language' missing in the request body"),
    
	INVALID_GITHUB_LANGUAGE("GHP03-400-04", "Invalid language"),
    
    INVALID_SORT("GHP03-400-05", 
    		"Invalid sort parameter: must be 'stars', 'forks', or 'updated'"), 
    
    // Client/Server Errors
    ERROR_CONNECTING_TO_EXTERNAL_SERVICE("GHP03-500-06", 
    		"Error connecting to external service"),
	
	EMPTY_SEARCH_RESULTS("GHP03-400-07", "GitHub search returned no results"), 
	
	GITHUB_API_ERROR("GHP03-400-08", 
			"<Dynamically prepare based on github error response"), 
	
	INVALID_GITHUB_RESPNOSE("GHP03-400-09", 
			"Recieved Invalid response for Github API"), 
	
	GITHUB_RATE_LIMIT_EXCEEDED("GHP03-400-10", 
			"GitHub API rate limit exceeded. Please try again later."); 
	

	
	private final String errorCode;
	private final String errorMessage;
	
	private ErrorCodeEnum(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	
	
	
	
	

}
