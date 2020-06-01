package com.tourism.canada.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.canada.dto.SuccessResponse;
import com.tourism.canada.entities.City;
import com.tourism.canada.entities.NationalPark;
import com.tourism.canada.exception.UserRelatedException;
import com.tourism.canada.service.NationalParkService;

@RestController
@RequestMapping(value = "/api/national-parks")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NationalParkController {
	
	private static final Logger logger = LoggerFactory.getLogger(NationalParkController.class);
	
	
	@Autowired
	private NationalParkService nationalParkservice;
	
	

	@RequestMapping(value = "/allParks")
	public Iterable<NationalPark> getAllNationalPark() {
		return nationalParkservice.getAllNationalPark();
	}

	@RequestMapping(value = "/province/{provinceid}/NationalPark")
	public Iterable<NationalPark> getAllNationalPark(@PathVariable Integer provinceid) {
		// System.out.println(id);
		return nationalParkservice.getAllNationalPark(provinceid);
	}

	@RequestMapping(value = "/city/{id}/NationalPark")
	public Iterable<NationalPark> getAllNationalParkByCity(@PathVariable Integer id) {
		return nationalParkservice.getAllNationalParkByCity(id);
	}

	@RequestMapping(value = "/NationalPark/{id}")
	public NationalPark getNationalPark(@PathVariable Integer id) {
		return nationalParkservice.getNationalPark(id);
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "/city/{cityId}/NationalPark")
	public NationalPark addNationalPark(@RequestBody NationalPark nationalPark, @PathVariable Integer cityId) {
		NationalPark response;
		nationalPark.setCity(new City(cityId));
		response = nationalParkservice.addNationalPark(nationalPark);
		return response;
	}


	@RequestMapping(method = RequestMethod.PUT, value = "/city/{cityId}/NationalPark")
	public NationalPark updateNationalPark(@RequestBody NationalPark nationalPark, @PathVariable Integer cityId) {
		NationalPark response;
		nationalPark.setCity(new City(cityId));
		response = nationalParkservice.updateNationalPark(nationalPark);
		return response;
	}


	@RequestMapping(method = RequestMethod.DELETE, value = "/province/{provinceId}/NationalPark/{id}")
	public SuccessResponse deleteNationalPark(@PathVariable Integer id) {
		try {
			nationalParkservice.deleteNationalPark(id);
			return new SuccessResponse("National Park deleted successfully");
		} catch (EmptyResultDataAccessException exe) {
			throw new UserRelatedException("No national park exist for the provided Id");
		}

	}
	
	

}
