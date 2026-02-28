package com.mycompany.repositories.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1/test")
public class TestController { 
	
	@Value("${myTestKey}")
	private String myTestKey;
	
	@GetMapping("add")
	public int test(@RequestParam int val1, @RequestParam int val2) {
		log.info("Inside test(): values recieved in val1:{}|val2:{}", val1, val2);
		
		int sumResult = val1 + val2;
		log.info("Result recieved in sumResult: {}", sumResult);
		return sumResult;
	}
	
	@PostConstruct
	public void init() {
		log.info("Inside init(): myTestKey value: {}", myTestKey);
	}
	
}
