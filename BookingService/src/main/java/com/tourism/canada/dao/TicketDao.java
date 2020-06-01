package com.tourism.canada.dao;

import org.springframework.data.repository.CrudRepository;

import com.tourism.canada.entities.Ticket;


public interface TicketDao extends CrudRepository<Ticket, Integer> {
	public Ticket findByTransactionTransactionId(Integer id);
}
