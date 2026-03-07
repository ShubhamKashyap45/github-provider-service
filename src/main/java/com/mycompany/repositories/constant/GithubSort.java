package com.mycompany.repositories.constant;

public enum GithubSort {
	
    STARS("stars"),
    FORKS("forks"),
    UPDATED("updated");
	
	private final String value;

	private GithubSort(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
	

}
