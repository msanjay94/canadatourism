package com.tourism.canada.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.canada.dto.SuccessResponse;
import com.tourism.canada.entities.Beaches;
import com.tourism.canada.entities.City;
import com.tourism.canada.exception.UserRelatedException;
import com.tourism.canada.service.BeachService;


@RestController
@RequestMapping(value = "/api/beaches")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BeachesController {
	@Autowired
	private BeachService beachservice;

	@RequestMapping(value = "/allBeaches")
	public Iterable<Beaches> getAllNationalPark() {
		return beachservice.getAllBeaches();
	}

	@RequestMapping(value = "/province/{provinceid}/beaches")
	public Iterable<Beaches> getAllBeaches(@PathVariable Integer provinceid) {
		return beachservice.getAllBeaches(provinceid);
	}

	@RequestMapping(value = "/city/{id}/beaches")
	public Iterable<Beaches> getBeaches(@PathVariable Integer id) {
		return beachservice.getAllBeachesByCity(id);
	}

	@RequestMapping(value = "/beach/{id}")
	public Beaches getNationalPark(@PathVariable Integer id) {
		return beachservice.getBeach(id);
	}


	@RequestMapping(method = RequestMethod.POST, value = "/province/{provinceId}/beach")
	public Beaches addBeaches(@RequestBody Beaches beach, @PathVariable Integer cityId) {
		beach.setCity(new City(cityId));
		Beaches returnbeach = beachservice.addBeach(beach);
		return returnbeach;
	}


	@RequestMapping(method = RequestMethod.PUT, value = "/province/{provinceId}/beach")
	public Beaches updateBeaches(@RequestBody Beaches beach, @PathVariable Integer cityId) {
		beach.setCity(new City(cityId));
		Beaches returnbeach = beachservice.updateBeach(beach);
		return returnbeach;
	}


	@RequestMapping(method = RequestMethod.DELETE, value = "/province/beach/{id}")
	public SuccessResponse deleteBeaches(@PathVariable Integer id) {
		try {
			beachservice.deleteBeach(id);
			return new SuccessResponse("Beach Deleted Successfully");
		} catch (EmptyResultDataAccessException exe) {
			throw new UserRelatedException("No beach exist for the provided Id");
		}
	}

}
