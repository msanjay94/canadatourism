package com.tourism.canada.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tourism.canada.entities.Provinces;


public interface ProvinceDao extends CrudRepository<Provinces, Integer> {

	@Transactional(readOnly = true)
	Optional<Provinces> findByProvinceName(String provinceName);
}
