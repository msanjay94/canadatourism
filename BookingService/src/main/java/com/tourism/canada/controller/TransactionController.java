package com.tourism.canada.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.canada.dto.SuccessResponse;
import com.tourism.canada.dto.TransactionDTO;
import com.tourism.canada.entities.Booking;
import com.tourism.canada.entities.Transaction;
import com.tourism.canada.service.BookingService;
import com.tourism.canada.service.TransactionService;

@RestController
@RequestMapping(value="/api/transaction")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionController {
	
	@Autowired	
	private TransactionService transactionService;
	
	@Autowired	
	private BookingService bookingService;

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@RequestMapping(value="/booking/{bookingid}/transaction")
	public TransactionDTO getAllTransaction(@PathVariable Integer bookingid) {
		return transactionService.getAllTransaction(bookingid);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@RequestMapping(method=RequestMethod.POST,value="/booking/makePayment")
	public SuccessResponse addBooking(@RequestBody @Valid TransactionDTO transactionDTO) {
		TransactionDTO responseDTO = transactionService.addTransaction(transactionDTO);
		SuccessResponse response = new SuccessResponse("Transaction Successful: Payment is done for the Booking.", responseDTO);
		return response;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.PUT,value="/booking/{bookingId}/Transaction/{id}")
	public void updateBooking(@RequestBody Transaction Transaction, @PathVariable Integer bookingId,@PathVariable String id) {
		Transaction.setBooking(new Booking(bookingId));
		transactionService.updateTransaction(Transaction);
	}

    @PreAuthorize("hasAnyRole('ROLE_USER')")
	@RequestMapping(method=RequestMethod.POST,value="/delete/{id}")
	public void deleteTransaction(@PathVariable Integer id) {
		TransactionDTO transaction = transactionService.getAllTransaction(id);
		if(null != transaction) {
			transactionService.deleteTransaction(transaction.getTransactionId());
		}
		bookingService.deleteBooking(id);
	}

}
