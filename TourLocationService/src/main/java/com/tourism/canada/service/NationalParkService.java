package com.tourism.canada.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.tourism.canada.dao.NationalParkDao;
import com.tourism.canada.entities.Beaches;
import com.tourism.canada.entities.NationalPark;

@Service
public class NationalParkService {

	@Autowired
	private NationalParkDao nationalParkDao;
	


	public Iterable<NationalPark> getAllNationalPark(int id) {
		ArrayList<NationalPark> parks= nationalParkDao.findByProvincesProvinceId(id);
		
		return parks;
	}
	
	public Iterable<NationalPark> getAllNationalParkByCity(Integer id) {
		ArrayList<NationalPark> parks= nationalParkDao.findByCityCityId(id);
		
		
		
		return parks;
	}
	
	public Iterable<NationalPark> getAllNationalPark() {
		ArrayList<NationalPark> parks= (ArrayList<NationalPark>) nationalParkDao.findAll();
		
		return parks;
	}

	public NationalPark getNationalPark(Integer NationalParkId) {
		NationalPark n= nationalParkDao.findById(NationalParkId).get();
		
		return n;
	}

	public void deleteNationalPark(Integer NationalParkId) {
		nationalParkDao.deleteById(NationalParkId);

	}

	public NationalPark updateNationalPark(NationalPark NationalPark) {
		NationalPark response;
		response = nationalParkDao.save(NationalPark);
		return response;
	}

	public NationalPark addNationalPark(NationalPark NationalPark) {
		NationalPark response;
		response = nationalParkDao.save(NationalPark);
		return response;
	}

	public ArrayList<NationalPark> search(String parkName) {
		ArrayList<NationalPark> parkList=nationalParkDao.findByNationalParkNameContainingIgnoreCase(parkName);
		return parkList;
	}
}
