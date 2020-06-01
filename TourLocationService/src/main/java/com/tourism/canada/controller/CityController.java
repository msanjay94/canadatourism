package com.tourism.canada.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.canada.entities.City;
import com.tourism.canada.service.CityServices;

@RestController
@RequestMapping(value="/api/cities")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CityController {

	@Autowired
	CityServices cityService;
	
	@GetMapping("/getcities")
	public ArrayList<City> getAllCities(){
		return cityService.getAllCities();
	}
	
	@GetMapping("/getcities/{provinceId}")
	public ArrayList<City> getAllCities(@PathVariable int provinceId){
		return cityService.getcitiesOfProvince(provinceId);
	}
}
