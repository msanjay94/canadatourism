package com.tourism.canada;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tourism.canada.service.LoaderService;

@SpringBootApplication
public class TouristLocationServiceApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(TouristLocationServiceApplication.class, args);
		
	}

}
