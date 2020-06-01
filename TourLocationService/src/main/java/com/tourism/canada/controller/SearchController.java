package com.tourism.canada.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.canada.entities.Beaches;
import com.tourism.canada.entities.NationalPark;
import com.tourism.canada.service.BeachService;
import com.tourism.canada.service.NationalParkService;

import net.minidev.json.JSONArray;

@RestController
@RequestMapping(value = "/api/search")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SearchController {
	
	@Autowired
	private BeachService beachservice;

	
	
	@Autowired
	private NationalParkService nationalParkservice;
	

	
	@RequestMapping(value = "/")
	public JSONArray search(@RequestParam String key ) {
		JSONArray jArray = new JSONArray();
		Iterable<Beaches> beachlist = beachservice.search(key);
		Iterable<NationalPark> parklist = nationalParkservice.search(key);
		jArray.add(beachlist);
		jArray.add(parklist);
		return jArray;
	}


}
