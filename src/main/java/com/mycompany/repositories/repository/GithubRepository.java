package com.mycompany.repositories.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.repositories.entity.GithubRepositoryEntity;

public interface GithubRepository extends JpaRepository<GithubRepositoryEntity, Long>{
	
	
}
