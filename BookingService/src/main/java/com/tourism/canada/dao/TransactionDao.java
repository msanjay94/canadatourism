package com.tourism.canada.dao;

import org.springframework.data.repository.CrudRepository;

import com.tourism.canada.entities.Transaction;

public interface TransactionDao extends CrudRepository<Transaction, Integer> {
	
	
	public Transaction findByBookingBookingId(Integer id);
}
