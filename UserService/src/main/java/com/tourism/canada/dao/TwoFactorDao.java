package com.tourism.canada.dao;

import org.springframework.data.repository.CrudRepository;

import com.tourism.canada.entities.TwoFactor;

public interface TwoFactorDao extends CrudRepository<TwoFactor, Integer> {

	public TwoFactor findTop1ByUserDetailsCustomerEmailOrderByTwoFactorIdDesc(String customerEmail);
	// public Optional<TwoFactor> findByUserDetailsCustomerEmail(String
	// customerEmail);
}
