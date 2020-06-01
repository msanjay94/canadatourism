package com.tourism.canada.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import com.tourism.canada.dao.CityDao;
import com.tourism.canada.entities.City;

@Service
public class CityServices {

	@Autowired
	CityDao cityDao;

	public ArrayList<City> getAllCities() {
		return cityDao.getCities();
	}

	public ArrayList<City> getcitiesOfProvince(int provinceId) {
		return cityDao.getCitiesOfProvince(provinceId);
	}

	public City getAllCities(@PathVariable int cityId) {
		return cityDao.getOne(cityId);
	}

}
