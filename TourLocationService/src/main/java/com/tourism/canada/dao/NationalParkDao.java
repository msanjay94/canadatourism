package com.tourism.canada.dao;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tourism.canada.entities.Beaches;
import com.tourism.canada.entities.NationalPark;

public interface NationalParkDao extends CrudRepository<NationalPark, Integer> {
	
	@Query("select np from NationalPark np where np.city.cityId IN (select c.cityId from City c where c.province.provinceId = :provinceId)")
	public ArrayList<NationalPark> findByProvincesProvinceId(@Param("provinceId") int id);
	
	@Query("select np from NationalPark np where np.city.cityId = :cityId")
	public ArrayList<NationalPark> findByCityCityId(@Param("cityId") int id);
	
	public Optional<NationalPark> findByNationalParkId(Integer id);
	
	public ArrayList<NationalPark> findByNationalParkNameContainingIgnoreCase(String parkName);
}
