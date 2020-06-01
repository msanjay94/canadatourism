

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;

public class Service {

	static void run(String ip, int port)
			throws MalformedURLException, IOException {
		int s = 0, f = 0;
		String jsonInputString = "{\"customerFname\": \"Nihir\", \"customerLname\": \"Shah\",\"customerNum\": \"9024023110\",\"customerEmail\": \"nh244735@dal.ca\",\"customerPassword\": \"Test@123\",\"is2faEnabled\": \"false\"}";
		System.out.println("Running Test case #1 : Calling register");		
		JSONObject httpResponse = HTTPUtil.getHttpResponse(
				"http://" + "ec2-18-233-13-69.compute-1.amazonaws.com" + ":" + "8081" + "/canadatourism/api/customer/user/register", HTTPUtil.HTTP_POST,jsonInputString);

		System.out.println("Received response code : "
				+ httpResponse.getInt(HTTPUtil.RESPONSE_CODE));
		if (httpResponse.getInt(HTTPUtil.RESPONSE_CODE) == 200 || httpResponse.getInt(HTTPUtil.RESPONSE_CODE) == 400 ) {
			s++;
			System.out.println("Test case passed");
		} else {
			System.out.println("Test case failed");
			f++;
		}
		System.out.println("========================================================");
		/*******************************************************************/

		String loginString = "{\"username\": \"nh244735@dal.ca\",\"password\": \"Test@123\"}";
		System.out.println("Running Test case #2 : Calling correct login");		
		JSONObject loginResponse = HTTPUtil.getHttpResponse(
				"http://" + "ec2-18-233-13-69.compute-1.amazonaws.com" + ":" + "8081" + "/canadatourism/api/customer/authenticate", HTTPUtil.HTTP_POST,loginString);
		
		String responseData = loginResponse.getString(HTTPUtil.RESPONSE_DATA);
		JSONObject jsonObject = new JSONObject(responseData);
		String jwtToken = jsonObject.getString("token");
		System.out.println("Received response code : "
				+ loginResponse.getInt(HTTPUtil.RESPONSE_CODE));
		if (loginResponse.getInt(HTTPUtil.RESPONSE_CODE) == 200) {
			s++;
			System.out.println("Test case passed");
		} else {
			System.out.println("Test case failed");
			f++;
		}
		System.out.println("========================================================");
		/************************************************************************/
		String loginStringFake = "{\"username\": \"nh244735@dal.ca\",\"password\": \"Test@1234\"}";
		System.out.println("Running Test case #3 : Calling fake login");	
		JSONObject loginResponseFake = HTTPUtil.getHttpResponse(
				"http://" + "ec2-18-233-13-69.compute-1.amazonaws.com" + ":" + "8081" + "/canadatourism/api/customer/authenticate", HTTPUtil.HTTP_POST,loginStringFake);
		System.out.println("Received response code : "
				+ loginResponseFake.getInt(HTTPUtil.RESPONSE_CODE));
		if (loginResponseFake.getInt(HTTPUtil.RESPONSE_CODE) == 400) {
			s++;
			System.out.println("Test case passed");
		} else {
			System.out.println("Test case failed");
			f++;
		}
		System.out.println("========================================================");
		/***************************************************************************/
		String busAvailability = "{\"journeyDate\": \"2020-03-29\",\"sourceLocationId\": \"32\",\"destinationLocationId\":\"4\"}";
		System.out.println("Running Test case #4 : Calling BusAvilabilty");		
		
		JSONObject busAvailabilityResponse = HTTPUtil.getHttpResponse(
				"http://" + "ec2-18-233-13-69.compute-1.amazonaws.com" + ":" + "8083" + "/canadatourism/api/bus/getavailibility", HTTPUtil.HTTP_POST,busAvailability);
		System.out.println("Received response code : "
				+ busAvailabilityResponse.getInt(HTTPUtil.RESPONSE_CODE));
		if (busAvailabilityResponse.getInt(HTTPUtil.RESPONSE_CODE) == 200) {
			s++;
			System.out.println("Test case passed");
		} else {
			System.out.println("Test case failed");
			f++;
		}
		System.out.println("========================================================");
		/*************************************************************************************/
		String jsonBooking = "{\"busId\": \"2\", \"journeyDate\": \"2020-03-31\",\"sourceLocationId\": \"4\",\"destinationLocationId\": \"32\",\"noOfSeats\": \"1\"}";
		System.out.println("Running Test case #5 : Calling Booking");		
		JSONObject httpBookingResponse = HTTPUtil.getHttpResponse(
				"http://" + "ec2-18-233-13-69.compute-1.amazonaws.com" + ":" + "8083" + "/canadatourism/api/bus/booking/makebooking", HTTPUtil.HTTP_POST,jsonBooking,jwtToken);

		System.out.println("Received response code : "
				+ httpBookingResponse.getInt(HTTPUtil.RESPONSE_CODE));

		String bookresponseData = httpBookingResponse.getString(HTTPUtil.RESPONSE_DATA);
		JSONObject jsonBookObject = new JSONObject(bookresponseData.toString());
		int bookingId = jsonBookObject.getInt("bookingId");
		if (httpBookingResponse.getInt(HTTPUtil.RESPONSE_CODE) == 200) {
			s++;
			System.out.println("Test case passed");
		} else {
			System.out.println("Test case failed");
			f++;
		}
		String passBookingId = String.valueOf(bookingId);
		System.out.println("========================================================");
		/************************************************************************************/
		String jsonFakeTransaction= "{\"transactionAmount\": \"200\", \"bookingId\": "+passBookingId+",\"cardNumber\": \"4716229541234556\"}";
		System.out.println("Running Test case #6 : Making transaction with dummy card");		
		JSONObject httpFakeTransaction = HTTPUtil.getHttpResponse(
				"http://" + "ec2-18-233-13-69.compute-1.amazonaws.com" + ":" + "8083" + "/canadatourism/api/transaction/booking/makePayment", HTTPUtil.HTTP_POST,jsonFakeTransaction,jwtToken);

		System.out.println("Received response code : "
				+ httpFakeTransaction.getInt(HTTPUtil.RESPONSE_CODE));

		if (httpFakeTransaction.getInt(HTTPUtil.RESPONSE_CODE) == 400) {
			s++;
			System.out.println("Test case passed");
		} else {
			System.out.println("Test case failed");
			f++;
		}
		System.out.println("========================================================");
		/***************************************************************************************/
		
		/************************************************************************************/
		String jsonTransaction= "{\"transactionAmount\": \"200\", \"bookingId\": "+passBookingId+",\"cardNumber\": \"4111111111111111\"}";
		System.out.println("Running Test case #7 : making proper transaction");		
		JSONObject httpTransaction = HTTPUtil.getHttpResponse(
				"http://" + "ec2-18-233-13-69.compute-1.amazonaws.com" + ":" + "8083" + "/canadatourism/api/transaction/booking/makePayment", HTTPUtil.HTTP_POST,jsonTransaction,jwtToken);

		System.out.println("Received response code : "
				+ httpTransaction.getInt(HTTPUtil.RESPONSE_CODE));

		if (httpTransaction.getInt(HTTPUtil.RESPONSE_CODE) == 200) {
			s++;
			System.out.println("Test case passed");
		} else {
			System.out.println("Test case failed");
			f++;
		}
		System.out.println("========================================================");
		/***************************************************************************************/
		System.out.println("Running Test case #8 : Deleteing transaction");		
		JSONObject httpdeleteTransaction = HTTPUtil.getHttpResponse(
				"http://" + "ec2-18-233-13-69.compute-1.amazonaws.com" + ":" + "8083" + "/canadatourism/api/transaction/delete/"+passBookingId, HTTPUtil.HTTP_POST,null,jwtToken);

		System.out.println("Received response code : "
				+ httpdeleteTransaction.getInt(HTTPUtil.RESPONSE_CODE));

		if (httpdeleteTransaction.getInt(HTTPUtil.RESPONSE_CODE) == 200) {
			s++;
			System.out.println("Test case passed");
		} else {
			System.out.println("Test case failed");
			f++;
		}
		System.out.println("========================================================");
		/***************************************************************************************/
		System.out.println("Total test cases: 8");
		System.out.println("Test cases passed: " + s);
		System.out.println("Test cases failed: " + f);
	}
}
