package com.tourism.canada.controller;

import java.net.URI;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tourism.canada.entities.Role;
import com.tourism.canada.entities.User;
import com.tourism.canada.exception.UserRelatedException;
import com.tourism.canada.jwtutility.JwtRequest;
import com.tourism.canada.jwtutility.JwtResponse;
import com.tourism.canada.jwtutility.JwtTokenUtil;
import com.tourism.canada.service.CustomerManagementService;
import com.tourism.canada.service.TwoFactorService;

import net.minidev.json.JSONArray;

/**
 * @author Mayank
 *
 */
@RestController
@RequestMapping(value = "/api/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConsumerUserController {

	@Autowired
	private CustomerManagementService customerManagementService;

	@Autowired
	private TwoFactorService twoFactorService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping(value = "/user/register", consumes = "application/json;charset=UTF-8")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<?> addUser(@Valid @RequestBody User customer) {
		System.out.println(customer);
		customer.setCustomerType(new Role(2, "USER"));
		User user = customerManagementService.addUser(customer);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/consumer/users/profile").build()
				.toUri();
		return ResponseEntity.created(location).allow(HttpMethod.GET).body(user);
	}

	@GetMapping("/user/profile")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public User getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = customerManagementService.getCustomerByEmail(username).get();
		return user;
	}
	
	@GetMapping("/getuser/{username}")
	public User getUser(@PathVariable String username) {

		User user = customerManagementService.getCustomerByEmail(username).get();
		return user;
	}
	
	@GetMapping("/getuserdetails/{username}")
	public UserDetails getUserDetails(@PathVariable String username) {

		UserDetails userDetails = customerManagementService.loadUserByUsername(username);
		return userDetails;
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PutMapping(value = "/user/profile")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<?> updateUser(@Valid @RequestBody User userForUpdate,
			@RequestHeader(required = false) String Authorization) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String token = Authorization;
		String username = authentication.getName();
		User user = customerManagementService.updateUser(userForUpdate, username);

		if (!user.getCustomerEmail().equals(username)) {
			final UserDetails userDetails = customerManagementService.loadUserByUsername(user.getCustomerEmail());

			token = jwtTokenUtil.generateToken(userDetails);
		}

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/consumer/users/profile").build()
				.toUri();

		JSONArray json = new JSONArray();
		json.add(user);
		json.add(new JwtResponse(token));
		return ResponseEntity.created(location).allow(HttpMethod.GET).body(json);
	}


	@RequestMapping(value = "/verifytoken", method = RequestMethod.POST)
	public String validateusertoken(@RequestBody String email) throws Exception {
		return customerManagementService.getUserFullName(email.replaceAll("\"", ""));
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public JwtResponse validateuser(@RequestBody JwtRequest request) throws Exception {

		authenticate(request.getUsername(), request.getPassword());

		Map<UserDetails, Boolean> details = customerManagementService.loadUserByUsernames(request.getUsername());

		UserDetails userDetails = null;
		boolean is2FAenabled = false;
		for (Map.Entry<UserDetails, Boolean> entry : details.entrySet()) {
			userDetails = entry.getKey();
			is2FAenabled = entry.getValue();
		}

		final String token = jwtTokenUtil.generateToken(userDetails);
		if (is2FAenabled) {
			JwtResponse response= new JwtResponse("");
			response.setVerified(false);
			String message = twoFactorService.requestTwoFactor(userDetails.getUsername(), token);
			response.setUserFullname(customerManagementService.getUserFullName(request.getUsername()));
			response.setMessage(message);
			return response;
		} else {
			JwtResponse response= new JwtResponse(token);
			response.setMessage("User Verified and Logged In successfully");
			response.setUserFullname(customerManagementService.getUserFullName(request.getUsername()));
			response.setVerified(true);
			return response;
		}
	}
	
	private Authentication authenticate(String username, String password) throws Exception {
		Authentication authObject;
		try {
			authObject = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new UserRelatedException("USER_IS_DISABLED");
		} catch (BadCredentialsException e) {
			throw new UserRelatedException("INVALID_CREDENTIAL: Wrong Password");
		}
		return authObject;
	}

	
	@RequestMapping(value = "/validate2FA/{username}/{code}", method = RequestMethod.POST)
	public JwtResponse posttwofactor(@PathVariable String code, @PathVariable String username) {
		
		//System.out.println(code+","+username);
		//TwoFactorService twoFactorService=new TwoFactorService(username, code);
		String token = twoFactorService.validateTwoFactor(username, code);
				
		if(!token.isEmpty()) {
			JwtResponse response= new JwtResponse(token);
			response.setVerified(true);
			response.setUserFullname(customerManagementService.getUserFullName(username));
			response.setMessage("User Verified and Logged In successfully");
			return response;
		}
		else {
			JwtResponse response= new JwtResponse(token);
			response.setVerified(false);
			response.setUserFullname(customerManagementService.getUserFullName(username));
			response.setMessage("Authentication failed. PLease verify the OTP again");
			return response;
		}
	}
	
	@RequestMapping(value = "/validate2FA", method = RequestMethod.POST)
	public JwtResponse posttwofactorAndroid(@RequestParam String code, @RequestParam String username) {
		
		//System.out.println(code+","+username);
		//TwoFactorService twoFactorService=new TwoFactorService(username, code);
		String token = twoFactorService.validateTwoFactor(username, code);
				
		if(!token.isEmpty()) {
			JwtResponse response= new JwtResponse(token);
			response.setVerified(true);
			response.setUserFullname(customerManagementService.getUserFullName(username));
			response.setMessage("User Verified and Logged In successfully");
			return response;
		}
		else {
			JwtResponse response= new JwtResponse(token);
			response.setVerified(false);
			response.setUserFullname(customerManagementService.getUserFullName(username));
			response.setMessage("Authentication failed. PLease verify the OTP again");
			return response;
		}
	}
}
