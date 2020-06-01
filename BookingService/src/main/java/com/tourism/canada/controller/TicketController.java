package com.tourism.canada.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.tourism.canada.dto.SuccessResponse;
import com.tourism.canada.dto.TicketDTO;
import com.tourism.canada.dto.TransactionDTO;
import com.tourism.canada.entities.Booking;
import com.tourism.canada.entities.Ticket;
import com.tourism.canada.entities.Transaction;
import com.tourism.canada.service.BookingService;
import com.tourism.canada.service.TicketService;
import com.tourism.canada.service.TransactionService;
import com.tourism.canada.service.TwoFactorService;

@RestController
@RequestMapping(value="/api/ticket")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TicketController {
	
	@Autowired	
	private TicketService ticketService;
	
	@Autowired	
	private TransactionService transactionService;
	
	@Autowired	
	private BookingService bookingService;
	
	@Autowired	
	private TwoFactorService twoFactorService;
	
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@RequestMapping(value="/all/{bookingId}/transaction")
	@Transactional
	public SuccessResponse getTicket(@PathVariable Integer bookingId) {
		TransactionDTO transaction = transactionService.getAllTransaction(bookingId);
		Booking booking;
		if(transaction!=null)
		{
			booking = bookingService.getBooking(transaction.getBookingId()).get();
			booking.getUserDetails();
			booking.setPaid(true);
			bookingService.updateBooking(booking);
			try {
				twoFactorService.sendBookingConfirmation(booking,new Transaction(transaction));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*String result = ticketService.getTicket(transaction.getTransactionId());
		if(result.equalsIgnoreCase("failure")) {
			SuccessResponse response = new SuccessResponse("Mail Sending Failed");
			return response;
		}else {
			SuccessResponse response = new SuccessResponse("Mail Send Successfully");
			return response;
		}*/
		SuccessResponse response = new SuccessResponse("Mail Send Successfully");
		return response;
		
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.PUT,value="/ticket/{ticketId}")
	public void updateTicket(@RequestBody Ticket ticket, @PathVariable Integer ticketId) {
		ticket.setTransaction(new Transaction(ticketId));
		ticketService.updateTicket(ticket);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.DELETE,value="/delete/{ticketId}")
	public void deleteTicket(@PathVariable Integer ticketId) {
		ticketService.deleteTicket(ticketId);
	}


	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@RequestMapping(method=RequestMethod.POST,value="/ticket/generate")
	public SuccessResponse addTicket(@RequestBody @Valid TicketDTO ticketDTO) {
		TicketDTO responseDTO = null;
		responseDTO = ticketService.addTicket(ticketDTO);
		SuccessResponse response = new SuccessResponse("Transaction Successful: Payment is done for the Booking.", responseDTO);
		return response;
	}

	/*@RequestMapping(method=RequestMethod.GET,value="/transaction/tickets")
	public void addtransaction() throws FileNotFoundException, DocumentException {
		System.out.println("##############################################");
		System.out.println("##############################################");
		System.out.println("##############################################");
		System.out.println("##############################################");
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		document.open();
		
		PdfWriter.getInstance(document, new FileOutputStream(new File("test.pdf")));

        //open
        document.open();

        Paragraph p = new Paragraph();
        p.add("Thanks for ticket with canada tourism");
        p.setSpacingAfter(36f);
        p.setAlignment(Element.ALIGN_CENTER);

        document.add(p);

        Paragraph p2 = new Paragraph();
        p2.setSpacingAfter(36f);
        p2.add("Source: Hallifax"); //no alignment

        document.add(p2);
        
        Paragraph p3 = new Paragraph();
        p3.setSpacingAfter(36f);
        p3.add("Destination: Ottawa"); //no alignment

        document.add(p3);
        
        Paragraph p4 = new Paragraph();
        p4.setSpacingAfter(36f);
        p4.add("Journey Date: 20-02-2020"); //no alignment

        document.add(p4);
        
        
        Paragraph p5 = new Paragraph();
        p5.add("Ticket Date: 20-02-2020"); //no alignment
        p5.setSpacingAfter(36f);
        document.add(p5);
        
        
        Paragraph p6 = new Paragraph();
        p6.add("No of Seats: 4"); //no alignment
        p6.setSpacingAfter(36f);
        document.add(p6);
        
        Paragraph p9 = new Paragraph();
        p9.add("Amount Paid: "); 
        p9.setSpacingAfter(36f);
        document.add(p9);
        
        Paragraph p7= new Paragraph();
        p7.add("Card Number: ************3416 "); //no alignment
        p7.setSpacingAfter(36f);
        document.add(p7);

//        Font f = new Font();
//        f.setStyle(Font.BOLD);
//        f.setSize(8);
        
        Paragraph p8 = new Paragraph();
        p8.add("Happy Journey"); 
        p8.setSpacingAfter(36f);
        p8.setAlignment(Element.ALIGN_CENTER);
        document.add(p8);
        
        String filename = "F:\\Subjects\\Cloud-SaurabhDey\\Mayank-Branch\\24-03-2020\\cloud-project-Dev-Mayank\\CanadaTourismApp\\download.png";
        Image image = null;
		try {
			image = Image.getInstance(filename);
		} catch (BadElementException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image.setAlignment(Image.ALIGN_RIGHT);
        document.add(image);

        //close
        document.close();

        System.out.println("Done");
		
		
		document.close();
		System.out.println("##############################################");
		System.out.println("##############################################");
		System.out.println("##############################################");
		
	}*/
}
