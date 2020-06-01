
package com.tourism.canada.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.canada.dto.SuccessResponse;
import com.tourism.canada.entities.Provinces;
import com.tourism.canada.exception.UserRelatedException;
import com.tourism.canada.service.ProvinceService;

@RestController
@RequestMapping(value = "/api/provinces")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProvinceController {

	@Autowired
	private ProvinceService provinceservice;


	@PostMapping(value = "/create")
	public Provinces createProvince(@RequestBody Provinces Province) {
		return provinceservice.createProvince(Province);
	}

	@RequestMapping(value = "/province/{ProvinceId}")
	public Optional<Provinces> getProvinceById(@PathVariable("ProvinceId") Integer ProvinceId) {
		return provinceservice.getProvinceById(ProvinceId);
	}

	@GetMapping(value = "/province/allProvinces")
	public Iterable<Provinces> getAllBookedProvinces() {
		return provinceservice.getAllProvinces();
	}


	@DeleteMapping(value = "/province/{ProvinceId}")
	public SuccessResponse delete(@PathVariable("ProvinceId") Integer ProvinceId) {
		try {
			provinceservice.deleteProvince(ProvinceId);
			return new SuccessResponse("Province deleted successfully");
		} catch (EmptyResultDataAccessException exe) {
			throw new UserRelatedException("No province exist for the provided Id");
		}
	}


	@PutMapping(value = "/province")
	public Provinces updateProvince(@RequestBody Provinces Province) {
		return provinceservice.updateProvince(Province);
	}
}
