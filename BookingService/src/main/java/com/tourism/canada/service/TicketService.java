package com.tourism.canada.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.tourism.canada.dao.TicketDao;
import com.tourism.canada.dto.TicketDTO;
import com.tourism.canada.entities.Booking;
import com.tourism.canada.entities.Ticket;
import com.tourism.canada.entities.Transaction;

@Service
public class TicketService {
	@Autowired
	private TicketDao ticketDao;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private BookingService bookingService;

	@Autowired
	private TwoFactorService twoFactorService;
	
	public String getTicket(Integer id){
		try {
		
		Transaction transaction = transactionService.getTransaction(id).get();
		Booking booking;
		booking = bookingService.getBooking(transaction.getBooking().getBookingId()).get();
		booking.getUserDetails();
		booking.setPaid(true);
		bookingService.updateBooking(booking);
		twoFactorService.sendBookingConfirmation(booking,transaction);
		} catch (DocumentException e) {
			return "failure";
		}
		return "success";
	}
	
	public void deleteTicket(Integer ticketId) {
		ticketDao.deleteById(ticketId);
	}

	public void updateTicket(Ticket ticket) {
		ticketDao.save(ticket);
	}


	public TicketDTO addTicket(TicketDTO ticketDTO) {
		Transaction transaction = transactionService.getTransaction(ticketDTO.getTransactionId()).get();
		Booking booking = bookingService.getBooking(transaction.getBooking().getBookingId()).get();
		
		
		Ticket ticket = new Ticket(ticketDTO);
		ticket = ticketDao.save(ticket);
		
		if(ticket!=null)
		{
			Transaction transaction1 = transactionService.getTransaction(ticket.getTransaction().getTransactionId()).get();
			transaction1.setTransactionStatus(true);
			transactionService.updateTransaction(transaction1);
		}
		
		TicketDTO savedTicket= new TicketDTO(ticket);
		
		try {
			
			twoFactorService.sendBookingConfirmation(booking,transaction);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return savedTicket;
	}
}
