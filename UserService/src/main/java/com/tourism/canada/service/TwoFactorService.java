package com.tourism.canada.service;


import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.tourism.canada.dao.TwoFactorDao;
import com.tourism.canada.dao.UserDao;
import com.tourism.canada.entities.TwoFactor;
import com.tourism.canada.entities.User;



@Service
public class TwoFactorService {
	
	
	
	@Value("${from.email}")
	private String fromgmail;
	
	@Value("${from.password}")
	private String fromPassword;
	
	
	
	@Autowired
	private TwoFactorDao twoFactorDao;
	
	@Autowired
	private UserDao userDao;
	
	

	public String validateTwoFactor(String username, String code) {
		
		String valid = "";
		  TwoFactor twofactor = twoFactorDao.findTop1ByUserDetailsCustomerEmailOrderByTwoFactorIdDesc(username);
		  //TwoFactor factor = twofactor.get(twofactor.size()-1); 
		  //System.out.println(twofactor.toString());
		  
		if (twofactor.getCode().equalsIgnoreCase(code)) {
			//System.out.println("here");
			Date currentDate = new Date();
			Date expiryDate = twofactor.getExpireTime();
			int compare = currentDate.compareTo(expiryDate);
			//System.out.println(compare);
			if (compare <= 0) {
				valid = twofactor.getJwtToken();
			}
		}
		 
		return valid;
			
	}
	
	
	
	public String requestTwoFactor(String username, String token) {
		
		User user = userDao.findByCustomerEmail(username).get();
		
		if(user.isIs2faEnabled())
		{
			String email = user.getCustomerEmail();
			
			Date date= new Date();
			
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.HOUR_OF_DAY, 3);
		    
		    String otp = String.valueOf((int)((Math.random())*(10000-1000))+9999);
		    
			TwoFactor twoFactor = new TwoFactor(user,otp,calendar.getTime(), token);
			twoFactor =twoFactorDao.save(twoFactor);
			
			//System.out.println("------Expiry Time--------"+twoFactor.getExpireTime());
			
			//Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(twilioPhoneNumber),
			         //"Your authentication OTP from Canada Tourism is "+otp).create();
			String subject = "2FA : OTP for Login to Cloud Tourism.";
			String body ="Hi "+user.getCustomerFname()+",\nYour OTP for login is "+otp;
			Document document = new Document();
			send(fromgmail,fromPassword,email,subject,body);  
			
			return "OTP send to your Email Address: "+email;
		}
		else {
			return "2FA Disabled";
		}
		
		
	}
	
	


	public static void send(String from,String password,String to,String sub,String msg){  
        //Get properties object    		
        Properties props = new Properties();    
        props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.socketFactory.port", "465");    
        props.put("mail.smtp.socketFactory.class",    
                  "javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "465");    
        props.put("mail.smtp.starttls.enable","true");
        //get Session   
        Session session = Session.getInstance(props,    
         new javax.mail.Authenticator() {    
         protected PasswordAuthentication getPasswordAuthentication() {    
         return new PasswordAuthentication(from,password);  
         }    
        });    
        //compose message 
        System.out.println(from);
        System.out.println(password);
        try {    
         MimeMessage message = new MimeMessage(session);    
         message.addRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(to));    
         message.setSubject(sub);    
         message.setText(msg);    
         //send message  
         Transport.send(message);    
         System.out.println("message sent successfully");    
        } catch (MessagingException e) {throw new RuntimeException(e);}    
           
  }  
	
	
	
	
}
