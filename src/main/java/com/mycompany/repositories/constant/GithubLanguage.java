package com.mycompany.repositories.constant;

public enum GithubLanguage {

    JAVA("java"),
    PYTHON("python"),
    JAVASCRIPT("javascript"),
    GO("go"),
    RUBY("ruby"),
    C_SHARP("c#"),
    C_PLUS_PLUS("c++"),
    PHP("php"),
    TYPESCRIPT("typescript"),
    KOTLIN("kotlin");

    private final String value;

    GithubLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}