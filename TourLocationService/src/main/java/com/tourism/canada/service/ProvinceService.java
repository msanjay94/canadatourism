package com.tourism.canada.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourism.canada.dao.ProvinceDao;
import com.tourism.canada.entities.Provinces;
import com.tourism.canada.exception.UserRelatedException;

@Service
public class ProvinceService {
	@Autowired
	private ProvinceDao provincedao;

	public Provinces createProvince(Provinces province) {
		return provincedao.save(province);
	}

	public Optional<Provinces> getProvinceById(Integer provinceId) {
		return provincedao.findById(provinceId);
	}

	public Optional<Provinces> getProvinceByName(String provinceName) {
		return provincedao.findByProvinceName(provinceName);
	}

	public Iterable<Provinces> getAllProvinces() {
		return provincedao.findAll();
	}

	public void deleteProvince(Integer provinceId) {
		provincedao.deleteById(provinceId);
	}

	public Provinces updateProvince(Provinces province) {

		Provinces existing = provincedao.findByProvinceName(province.getProvinceName()).get();
		if (existing != null && existing.getProvinceId() != province.getProvinceId())
			throw new UserRelatedException("Province with the given name already in use");
		else if (existing != null && existing.getProvinceName() == province.getProvinceName())
			throw new UserRelatedException("No Change for Update");
		else
			return provincedao.save(province);
	}
}
