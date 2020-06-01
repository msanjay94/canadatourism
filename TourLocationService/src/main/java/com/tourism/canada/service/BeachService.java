package com.tourism.canada.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.tourism.canada.dao.BeachesDao;
import com.tourism.canada.entities.Beaches;
import com.tourism.canada.entities.NationalPark;

@Service
public class BeachService {

	@Autowired
	private BeachesDao beachdao;
	


	public Iterable<Beaches> getAllBeaches(int id) {
		ArrayList<Beaches> beachList=beachdao.findByProvincesProvinceId(id);
		
		return beachList;
	}

	public Iterable<Beaches> getAllBeachesByCity(Integer id) {
		ArrayList<Beaches> beachList=beachdao.findByCityCityId(id);
		
		return beachList;
	}

	public Iterable<Beaches> getAllBeaches() {
		ArrayList<Beaches> beachList=(ArrayList<Beaches>) beachdao.findAll();
		
		return beachList;
	}

	public Beaches getBeach(Integer BeachId) {
		Beaches beach = beachdao.findById(BeachId).get();
		
		return beach;
	}

	public void deleteBeach(Integer BeachId) {
		beachdao.deleteById(BeachId);
	}

	public Beaches updateBeach(Beaches Beach) {
		Beaches addedbeach = beachdao.save(Beach);
		return addedbeach;
	}

	public Beaches addBeach(Beaches Beach) {
		Beaches addedbeach = beachdao.save(Beach);
		return addedbeach;
	}
	
	public ArrayList<Beaches> search(String beachesName) {
		ArrayList<Beaches> beachList=beachdao.findByBeachesNameContainingIgnoreCase(beachesName);
		return beachList;
	}
}
