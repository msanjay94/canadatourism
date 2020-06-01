package com.tourism.canada.dao;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tourism.canada.entities.Beaches;

public interface BeachesDao extends CrudRepository<Beaches, Integer> {
	
	@Query("select np from Beaches np where np.city.cityId IN (select c.cityId from City c where c.province.provinceId = :provinceId)")
	public ArrayList<Beaches> findByProvincesProvinceId(@Param("provinceId") int id);
	
	@Query("select np from Beaches np where np.city.cityId = :cityId")
	public ArrayList<Beaches> findByCityCityId(@Param("cityId") int id);
	
	public ArrayList<Beaches> findByBeachesNameContainingIgnoreCase(String beachesName);

	
	public Optional<Beaches> findByBeachesId(Integer id);
}
