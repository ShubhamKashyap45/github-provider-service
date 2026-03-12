package com.mycompany.repositories.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "repositories")
@Data
public class GithubRepositoryEntity {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "language")
    private String language;

    @Column(name = "stars")
    private int stars;

    @Column(name = "forks")
    private int forks;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
