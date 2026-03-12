package com.mycompany.repositories.pojo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Repository {
	
    private long id;
    private String name;
    private String description;
    private String owner;    
    private String language;
    private int stars;
    private int forks;
    private LocalDateTime lastUpdated;

}
