package com.tourism.canada.service;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.tourism.canada.dao.TransactionDao;
import com.tourism.canada.dto.TransactionDTO;
import com.tourism.canada.entities.Booking;
import com.tourism.canada.entities.Transaction;


@Service
public class TransactionService {
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private TwoFactorService twoFactorService;
	
    public TransactionDTO getAllTransaction(Integer id) {
		Transaction transaction = transactionDao.findByBookingBookingId(id);
		if(null == transaction) {
			return null;
		}
		TransactionDTO responseTrans = new TransactionDTO();
		responseTrans = new TransactionDTO(transaction);
		return responseTrans;
	}
	
	public Optional<Transaction> getTransaction(Integer transactionId) {
		return transactionDao.findById(transactionId);
	}
	
	public void deleteTransaction(Integer transactionId) {
		transactionDao.deleteById(transactionId);
	}

	public void updateTransaction(Transaction transaction) {
		transactionDao.save(transaction);
	}
	
	@Transactional
	public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
		Transaction transaction = new Transaction(transactionDTO);
		transaction.setTransactionStatus(true);
		transaction.setTransactionDate(new Date());
		transaction = transactionDao.save(transaction);
		Booking booking;
		if(transaction!=null)
		{
			booking = bookingService.getBooking(transaction.getBooking().getBookingId()).get();
			booking.getUserDetails();
			booking.setPaid(true);
			bookingService.updateBooking(booking);
			try {
				twoFactorService.sendBookingConfirmation(booking,transaction);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		TransactionDTO savedTransaction= new TransactionDTO(transaction);
		
		return savedTransaction;
	}
}
